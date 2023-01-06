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
package io.aeron.command;

import org.agrona.MutableDirectBuffer;

import static org.agrona.BitUtil.SIZE_OF_INT;
import static org.agrona.BitUtil.SIZE_OF_LONG;

/**
 * Message to denote that new buffers have been added for a subscription.
 * <p>
 * NOTE: Layout should be SBE compliant
 *
 * @see ControlProtocolEvents
 * <pre>
 *   0                   1                   2                   3
 *   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |                         Correlation ID                        |
 *  |                                                               |
 *  +---------------------------------------------------------------+
 *  |                          Session ID                           |
 *  +---------------------------------------------------------------+
 *  |                           Stream ID                           |
 *  +---------------------------------------------------------------+
 *  |                Subscriber Position Block Length               |
 *  +---------------------------------------------------------------+
 *  |                   Subscriber Position Count                   |
 *  +---------------------------------------------------------------+
 *  |                    Subscriber Position Id 0                   |
 *  +---------------------------------------------------------------+
 *  |                     Padding for alignment                     |
 *  +---------------------------------------------------------------+
 *  |                       Registration Id 0                       |
 *  |                                                               |
 *  +---------------------------------------------------------------+
 *  |                    Subscriber Position Id 1                   |
 *  +---------------------------------------------------------------+
 *  |                     Padding for alignment                     |
 *  +---------------------------------------------------------------+
 *  |                       Registration Id 1                       |
 *  |                                                               |
 *  +---------------------------------------------------------------+
 *  |                                                              ...
 * ...     Up to "Position Indicators Count" entries of this form   |
 *  +---------------------------------------------------------------+
 *  |                        Log File Length                        |
 *  +---------------------------------------------------------------+
 *  |                     Log File Name (ASCII)                    ...
 * ...                                                              |
 *  +---------------------------------------------------------------+
 *  |                     Source identity Length                    |
 *  +---------------------------------------------------------------+
 *  |                    Source identity (ASCII)                   ...
 * ...                                                              |
 *  +---------------------------------------------------------------+
 * </pre>
 */
public class ImageBuffersReadyFlyweight
{
    private static final int CORRELATION_ID_OFFSET = 0;
    private static final int SESSION_ID_OFFSET = CORRELATION_ID_OFFSET + SIZE_OF_LONG;
    private static final int STREAM_ID_FIELD_OFFSET = SESSION_ID_OFFSET + SIZE_OF_INT;
    private static final int SUBSCRIBER_POSITION_BLOCK_LENGTH_OFFSET = STREAM_ID_FIELD_OFFSET + SIZE_OF_INT;
    private static final int SUBSCRIBER_POSITION_COUNT_OFFSET = SUBSCRIBER_POSITION_BLOCK_LENGTH_OFFSET + SIZE_OF_INT;
    private static final int SUBSCRIBER_POSITIONS_OFFSET = SUBSCRIBER_POSITION_COUNT_OFFSET + SIZE_OF_INT;

    private static final int SUBSCRIBER_POSITION_BLOCK_LENGTH = SIZE_OF_INT + SIZE_OF_INT + SIZE_OF_LONG;

    private MutableDirectBuffer buffer;
    private int offset;

    /**
     * Wrap the buffer at a given offset for updates.
     *
     * @param buffer to wrap
     * @param offset at which the message begins.
     * @return for fluent API
     */
    public final ImageBuffersReadyFlyweight wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    /**
     * return correlation id field
     *
     * @return correlation id field
     */
    public long correlationId()
    {
        return buffer.getLong(offset + CORRELATION_ID_OFFSET);
    }

    /**
     * set correlation id field
     *
     * @param correlationId field value
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight correlationId(final long correlationId)
    {
        buffer.putLong(offset + CORRELATION_ID_OFFSET, correlationId);

        return this;
    }

    /**
     * return session id field
     *
     * @return session id field
     */
    public int sessionId()
    {
        return buffer.getInt(offset + SESSION_ID_OFFSET);
    }

    /**
     * set session id field
     *
     * @param sessionId field value
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight sessionId(final int sessionId)
    {
        buffer.putInt(offset + SESSION_ID_OFFSET, sessionId);

        return this;
    }

    /**
     * return stream id field
     *
     * @return stream id field
     */
    public int streamId()
    {
        return buffer.getInt(offset + STREAM_ID_FIELD_OFFSET);
    }

    /**
     * set stream id field
     *
     * @param streamId field value
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight streamId(final int streamId)
    {
        buffer.putInt(offset + STREAM_ID_FIELD_OFFSET, streamId);

        return this;
    }

    /**
     * return the number of position indicators
     *
     * @return the number of position indicators
     */
    public int subscriberPositionCount()
    {
        return buffer.getInt(offset + SUBSCRIBER_POSITION_COUNT_OFFSET);
    }

    /**
     * set the number of position indicators
     *
     * @param value the number of position indicators
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight subscriberPositionCount(final int value)
    {
        buffer.putInt(offset + SUBSCRIBER_POSITION_BLOCK_LENGTH_OFFSET, SUBSCRIBER_POSITION_BLOCK_LENGTH);
        buffer.putInt(offset + SUBSCRIBER_POSITION_COUNT_OFFSET, value);

        return this;
    }

    /**
     * Set the position Id for the subscriber
     *
     * @param index for the subscriber position
     * @param id    for the subscriber position
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight subscriberPositionId(final int index, final int id)
    {
        buffer.putInt(offset + subscriberPositionOffset(index), id);

        return this;
    }

    /**
     * Return the position Id for the subscriber
     *
     * @param index for the subscriber position
     * @return position Id for the subscriber
     */
    public int subscriberPositionId(final int index)
    {
        return buffer.getInt(offset + subscriberPositionOffset(index));
    }

    /**
     * Set the registration Id for the subscriber position
     *
     * @param index for the subscriber position
     * @param id    for the subscriber position
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight positionIndicatorRegistrationId(final int index, final long id)
    {
        buffer.putLong(offset + subscriberPositionOffset(index) + (SIZE_OF_INT * 2), id);

        return this;
    }

    /**
     * Return the registration Id for the subscriber position
     *
     * @param index for the subscriber position
     * @return registration Id for the subscriber position
     */
    public long positionIndicatorRegistrationId(final int index)
    {
        return buffer.getLong(offset + subscriberPositionOffset(index) + (SIZE_OF_INT * 2));
    }

    /**
     * Return the Log Filename in ASCII
     *
     * @return log filename
     */
    public String logFileName()
    {
        return buffer.getStringAscii(offset + logFileNameOffset());
    }

    /**
     * Set the log filename in ASCII
     *
     * @param logFileName for the image
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight logFileName(final String logFileName)
    {
        buffer.putStringAscii(offset + logFileNameOffset(), logFileName);
        return this;
    }

    /**
     * Return the source identity string in ASCII
     *
     * @return source identity string
     */
    public String sourceIdentity()
    {
        return buffer.getStringAscii(offset + sourceIdentityOffset());
    }

    /**
     * Set the source identity string in ASCII
     *
     * @param value for the source identity
     * @return flyweight
     */
    public ImageBuffersReadyFlyweight sourceIdentity(final String value)
    {
        buffer.putStringAscii(offset + sourceIdentityOffset(), value);
        return this;
    }

    /**
     * Get the length of the current message
     * <p>
     * NB: must be called after the data is written in order to be accurate.
     *
     * @return the length of the current message
     */
    public int length()
    {
        final int sourceIdentityOffset = sourceIdentityOffset();
        return sourceIdentityOffset + buffer.getInt(offset + sourceIdentityOffset) + SIZE_OF_INT;
    }

    private int subscriberPositionOffset(final int index)
    {
        return SUBSCRIBER_POSITIONS_OFFSET + (index * SUBSCRIBER_POSITION_BLOCK_LENGTH);
    }

    private int logFileNameOffset()
    {
        return subscriberPositionOffset(subscriberPositionCount());
    }

    private int sourceIdentityOffset()
    {
        final int logFileNameOffset = logFileNameOffset();
        return logFileNameOffset + buffer.getInt(offset + logFileNameOffset) + SIZE_OF_INT;
    }
}
