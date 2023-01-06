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
package io.aeron.driver.status;

import org.agrona.concurrent.status.CountersManager;
import org.agrona.concurrent.status.UnsafeBufferPosition;

/**
 * The position the Sender has reached for sending data to the media on a session-channel-stream tuple.
 */
public class SenderLimit
{
    /**
     * Type id of a sender position counter.
     */
    public static final int SENDER_LIMIT_TYPE_ID = 9;

    /**
     * Human readable name for the counter.
     */
    public static final String NAME = "snd-lmt";

    public static UnsafeBufferPosition allocate(
        final CountersManager countersManager,
        final long registrationId,
        final int sessionId,
        final int streamId,
        final String channel)
    {
        return StreamPositionCounter.allocate(
            NAME, SENDER_LIMIT_TYPE_ID, countersManager, registrationId, sessionId, streamId, channel);
    }
}
