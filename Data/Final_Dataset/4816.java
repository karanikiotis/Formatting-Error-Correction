/*
 * Copyright 2014-2017 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.aeron.driver;

import io.aeron.driver.status.SystemCounters;
import io.aeron.protocol.DataHeaderFlyweight;
import org.agrona.collections.BiInt2ObjectMap;
import org.agrona.concurrent.status.AtomicCounter;
import org.agrona.concurrent.NanoClock;

import static io.aeron.driver.Configuration.MAX_RETRANSMITS_DEFAULT;
import static io.aeron.driver.status.SystemCounterDescriptor.INVALID_PACKETS;

/**
 * Tracking and handling of retransmit request, NAKs, for senders, and receivers.
 * <p>
 * A max number of retransmits is permitted by {@link Configuration#MAX_RETRANSMITS_DEFAULT}. Additional received NAKs
 * will be ignored if this maximum is reached.
 */
public class RetransmitHandler
{
    private final BiInt2ObjectMap<RetransmitAction> activeRetransmitsMap = new BiInt2ObjectMap<>();
    private final RetransmitAction[] retransmitActionPool = new RetransmitAction[MAX_RETRANSMITS_DEFAULT];
    private final NanoClock nanoClock;
    private final FeedbackDelayGenerator delayGenerator;
    private final FeedbackDelayGenerator lingerTimeoutGenerator;
    private final AtomicCounter invalidPackets;

    /**
     * Create a retransmit handler.
     *
     * @param nanoClock              used to determine time
     * @param systemCounters         for recording significant events.
     * @param delayGenerator         to use for delay determination
     * @param lingerTimeoutGenerator to use for linger timeout
     */
    public RetransmitHandler(
        final NanoClock nanoClock,
        final SystemCounters systemCounters,
        final FeedbackDelayGenerator delayGenerator,
        final FeedbackDelayGenerator lingerTimeoutGenerator)
    {
        this.nanoClock = nanoClock;
        this.invalidPackets = systemCounters.get(INVALID_PACKETS);
        this.delayGenerator = delayGenerator;
        this.lingerTimeoutGenerator = lingerTimeoutGenerator;

        for (int i = 0; i < MAX_RETRANSMITS_DEFAULT; i++)
        {
            retransmitActionPool[i] = new RetransmitAction();
        }
    }

    /**
     * Called on reception of a NAK to start retransmits handling.
     *
     * @param termId           from the NAK and the term id of the buffer to retransmit from
     * @param termOffset       from the NAK and the offset of the data to retransmit
     * @param length           of the missing data
     * @param termLength       of the term buffer.
     * @param retransmitSender to call if an immediate retransmit is required
     */
    public void onNak(
        final int termId,
        final int termOffset,
        final int length,
        final int termLength,
        final RetransmitSender retransmitSender)
    {
        if (!isInvalid(termOffset, termLength))
        {
            if (null == activeRetransmitsMap.get(termId, termOffset) &&
                activeRetransmitsMap.size() < MAX_RETRANSMITS_DEFAULT)
            {
                final RetransmitAction action = assignRetransmitAction();
                action.termId = termId;
                action.termOffset = termOffset;
                action.length = Math.min(length, termLength - termOffset);

                final long delay = determineRetransmitDelay();
                if (0 == delay)
                {
                    retransmitSender.resend(termId, termOffset, action.length);
                    action.linger(determineLingerTimeout(), nanoClock.nanoTime());
                }
                else
                {
                    action.delay(delay, nanoClock.nanoTime());
                }

                activeRetransmitsMap.put(termId, termOffset, action);
            }
        }
    }

    /**
     * Called to indicate a retransmission is received that may obviate the need to send one ourselves.
     * <p>
     * NOTE: Currently only called from unit tests. Would be used for retransmitting from receivers for NAK suppression
     *
     * @param termId     of the data
     * @param termOffset of the data
     */
    public void onRetransmitReceived(final int termId, final int termOffset)
    {
        final RetransmitAction action = activeRetransmitsMap.get(termId, termOffset);

        if (null != action && State.DELAYED == action.state)
        {
            activeRetransmitsMap.remove(termId, termOffset);
            action.cancel();
            // do not go into linger
        }
    }

    /**
     * Called to process any outstanding timeouts.
     *
     * @param nowNs            time in nanoseconds
     * @param retransmitSender to call on retransmissions
     * @return count of expired actions performed
     */
    public int processTimeouts(final long nowNs, final RetransmitSender retransmitSender)
    {
        int result = 0;

        if (activeRetransmitsMap.size() > 0)
        {
            for (final RetransmitAction action : retransmitActionPool)
            {
                switch (action.state)
                {
                    case DELAYED:
                        if (nowNs > action.expireNs)
                        {
                            retransmitSender.resend(action.termId, action.termOffset, action.length);
                            action.linger(determineLingerTimeout(), nanoClock.nanoTime());
                            result++;
                        }
                        break;

                    case LINGERING:
                        if (nowNs > action.expireNs)
                        {
                            action.cancel();
                            activeRetransmitsMap.remove(action.termId, action.termOffset);
                            result++;
                        }
                        break;
                }
            }
        }

        return result;
    }

    private boolean isInvalid(final int termOffset, final int termLength)
    {
        final boolean isInvalid = (termOffset > (termLength - DataHeaderFlyweight.HEADER_LENGTH)) || (termOffset < 0);

        if (isInvalid)
        {
            invalidPackets.increment();
        }

        return isInvalid;
    }

    private long determineRetransmitDelay()
    {
        return delayGenerator.generateDelay();
    }

    private long determineLingerTimeout()
    {
        return lingerTimeoutGenerator.generateDelay();
    }

    private RetransmitAction assignRetransmitAction()
    {
        for (final RetransmitAction action : retransmitActionPool)
        {
            if (State.INACTIVE == action.state)
            {
                return action;
            }
        }

        throw new IllegalStateException("Maximum number of active RetransmitActions reached");
    }

    private enum State
    {
        DELAYED,
        LINGERING,
        INACTIVE
    }

    static final class RetransmitAction
    {
        long expireNs;
        int termId;
        int termOffset;
        int length;
        State state = State.INACTIVE;

        public void delay(final long delayNs, final long nowNs)
        {
            state = State.DELAYED;
            expireNs = nowNs + delayNs;
        }

        public void linger(final long timeoutNs, final long nowNs)
        {
            state = State.LINGERING;
            expireNs = nowNs + timeoutNs;
        }

        public void cancel()
        {
            state = State.INACTIVE;
        }
    }
}
