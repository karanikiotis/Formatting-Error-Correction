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
package io.aeron;

import io.aeron.logbuffer.*;
import org.agrona.*;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.status.ReadablePosition;

import static io.aeron.logbuffer.LogBufferDescriptor.*;
import static io.aeron.protocol.DataHeaderFlyweight.HEADER_LENGTH;

/**
 * Aeron Publisher API for sending messages to subscribers of a given channel and streamId pair. ExclusivePublications
 * each get their own session id so multiple can be concurrently active on the same media driver as independent streams.
 * <p>
 * {@link ExclusivePublication}s are created via the {@link Aeron#addExclusivePublication(String, int)} method,
 * and messages are sent via one of the {@link #offer(DirectBuffer)} methods, or a
 * {@link #tryClaim(int, ExclusiveBufferClaim)} and {@link ExclusiveBufferClaim#commit()} method combination.
 * <p>
 * {@link ExclusivePublication}s have the potential to provide greater throughput than {@link Publication}s.
 * <p>
 * The APIs used try claim and offer are non-blocking.
 * <p>
 * <b>Note:</b> ExclusivePublication instances are NOT threadsafe for offer and try claim method but are for position.
 *
 * @see Aeron#addExclusivePublication(String, int)
 * @see ExclusiveBufferClaim
 */
public class ExclusivePublication implements AutoCloseable
{
    /**
     * The publication is not yet connected to a subscriber.
     */
    public static final long NOT_CONNECTED = -1;

    /**
     * The offer failed due to back pressure from the subscribers preventing further transmission.
     */
    public static final long BACK_PRESSURED = -2;

    /**
     * The offer failed due to an administration action and should be retried.
     * The action is an operation such as log rotation which is likely to have succeeded by the next retry attempt.
     */
    public static final long ADMIN_ACTION = -3;

    /**
     * The {@link ExclusivePublication} has been closed and should no longer be used.
     */
    public static final long CLOSED = -4;

    private final long originalRegistrationId;
    private final long registrationId;
    private final int streamId;
    private final int sessionId;
    private final int initialTermId;
    private final int maxMessageLength;
    private final int maxPayloadLength;
    private final int positionBitsToShift;
    private long termBeginPosition;
    private int activePartitionIndex;
    private int termId;
    private int termOffset;

    private volatile boolean isClosed = false;

    private final ExclusiveTermAppender[] termAppenders = new ExclusiveTermAppender[PARTITION_COUNT];
    private final ReadablePosition positionLimit;
    private final UnsafeBuffer logMetaDataBuffer;
    private final HeaderWriter headerWriter;
    private final LogBuffers logBuffers;
    private final ClientConductor conductor;
    private final String channel;

    ExclusivePublication(
        final ClientConductor clientConductor,
        final String channel,
        final int streamId,
        final int sessionId,
        final ReadablePosition positionLimit,
        final LogBuffers logBuffers,
        final long originalRegistrationId,
        final long registrationId)
    {
        final UnsafeBuffer[] buffers = logBuffers.termBuffers();
        final UnsafeBuffer logMetaDataBuffer = logBuffers.metaDataBuffer();

        for (int i = 0; i < PARTITION_COUNT; i++)
        {
            termAppenders[i] = new ExclusiveTermAppender(buffers[i], logMetaDataBuffer, i);
        }

        final int termLength = logBuffers.termLength();
        this.maxPayloadLength = LogBufferDescriptor.mtuLength(logMetaDataBuffer) - HEADER_LENGTH;
        this.maxMessageLength = FrameDescriptor.computeExclusiveMaxMessageLength(termLength);
        this.conductor = clientConductor;
        this.channel = channel;
        this.streamId = streamId;
        this.sessionId = sessionId;
        this.initialTermId = LogBufferDescriptor.initialTermId(logMetaDataBuffer);
        this.logMetaDataBuffer = logMetaDataBuffer;
        this.originalRegistrationId = originalRegistrationId;
        this.registrationId = registrationId;
        this.positionLimit = positionLimit;
        this.logBuffers = logBuffers;
        this.positionBitsToShift = Integer.numberOfTrailingZeros(termLength);
        this.headerWriter = new HeaderWriter(defaultFrameHeader(logMetaDataBuffer));
        this.activePartitionIndex = activePartitionIndex(logMetaDataBuffer);

        final long rawTail = termAppenders[activePartitionIndex].rawTail();
        termId = termId(rawTail);
        termOffset = termOffset(rawTail, termLength);
        termBeginPosition = computeTermBeginPosition(termId, positionBitsToShift, initialTermId);
    }

    /**
     * Get the length in bytes for each term partition in the log buffer.
     *
     * @return the length in bytes for each term partition in the log buffer.
     */
    public int termBufferLength()
    {
        return logBuffers.termLength();
    }

    /**
     * Media address for delivery to the channel.
     *
     * @return Media address for delivery to the channel.
     */
    public String channel()
    {
        return channel;
    }

    /**
     * Stream identity for scoping within the channel media address.
     *
     * @return Stream identity for scoping within the channel media address.
     */
    public int streamId()
    {
        return streamId;
    }

    /**
     * Session under which messages are published. Identifies this {@link ExclusivePublication} instance.
     *
     * @return the session id for this publication.
     */
    public int sessionId()
    {
        return sessionId;
    }

    /**
     * The initial term id assigned when this {@link ExclusivePublication} was created. This can be used to determine
     * how many terms have passed since creation.
     *
     * @return the initial term id.
     */
    public int initialTermId()
    {
        return initialTermId;
    }

    /**
     * Maximum message length supported in bytes. Messages may be made of multiple fragments if greater than
     * MTU length.
     *
     * @return maximum message length supported in bytes.
     */
    public int maxMessageLength()
    {
        return maxMessageLength;
    }

    /**
     * Maximum length of a message payload that fits within a message fragment.
     * <p>
     * This is he MTU length minus the message fragment header length.
     *
     * @return maximum message fragment payload length.
     */
    public int maxPayloadLength()
    {
        return maxPayloadLength;
    }

    /**
     * Get the original registration used to register this Publication with the media driver by the first publisher.
     *
     * @return original registration id
     */
    public long originalRegistrationId()
    {
        return originalRegistrationId;
    }

    /**
     * Is this Publication the original instance added to the driver? If not then it was added after another client
     * has already added the publication.
     *
     * @return true if this instance is the first added otherwise false.
     */
    public boolean isOriginal()
    {
        return originalRegistrationId == registrationId;
    }

    /**
     * Get the registration id used to register this Publication with the media driver.
     * <p>
     * If this value is different from the {@link #originalRegistrationId()} then another client has previously added
     * this Publication. In the case of an exclusive publication this should never happen.
     *
     * @return registration id
     */
    public long registrationId()
    {
        return registrationId;
    }

    /**
     * Has the {@link ExclusivePublication} seen an active Subscriber recently?
     *
     * @return true if this {@link ExclusivePublication} has seen an active subscriber otherwise false.
     */
    public boolean isConnected()
    {
        return !isClosed && conductor.isPublicationConnected(timeOfLastStatusMessage(logMetaDataBuffer));
    }

    /**
     * Release resources used by this Publication.
     */
    public void close()
    {
        conductor.clientLock().lock();
        try
        {
            if (!isClosed)
            {
                isClosed = true;
                conductor.releasePublication(this);
            }
        }
        finally
        {
            conductor.clientLock().unlock();
        }
    }

    /**
     * Has this object been closed and should no longer be used?
     *
     * @return true if it has been closed otherwise false.
     */
    public boolean isClosed()
    {
        return isClosed;
    }

    /**
     * Forcibly close the Publication and release resources
     */
    void forceClose()
    {
        if (!isClosed)
        {
            isClosed = true;
            conductor.asyncReleasePublication(registrationId);
            conductor.lingerResource(managedResource());
        }
    }

    /**
     * Get the current position to which the publication has advanced for this stream.
     *
     * @return the current position to which the publication has advanced for this stream.
     * @throws IllegalStateException if the publication is closed.
     */
    public long position()
    {
        if (isClosed)
        {
            return CLOSED;
        }

        final long rawTail = rawTailVolatile(logMetaDataBuffer);
        final int termOffset = termOffset(rawTail, logBuffers.termLength());

        return computePosition(termId(rawTail), termOffset, positionBitsToShift, initialTermId);
    }

    /**
     * Get the position limit beyond which this {@link ExclusivePublication} will be back pressured.
     * <p>
     * This should only be used as a guide to determine when back pressure is likely to be applied.
     *
     * @return the position limit beyond which this {@link ExclusivePublication} will be back pressured.
     */
    public long positionLimit()
    {
        if (isClosed)
        {
            return CLOSED;
        }

        return positionLimit.getVolatile();
    }

    /**
     * Non-blocking publish of a buffer containing a message.
     *
     * @param buffer containing message.
     * @return The new stream position, otherwise {@link #NOT_CONNECTED}, {@link #BACK_PRESSURED},
     * {@link #ADMIN_ACTION}, or {@link #CLOSED}.
     */
    public long offer(final DirectBuffer buffer)
    {
        return offer(buffer, 0, buffer.capacity());
    }

    /**
     * Non-blocking publish of a partial buffer containing a message.
     *
     * @param buffer containing message.
     * @param offset offset in the buffer at which the encoded message begins.
     * @param length in bytes of the encoded message.
     * @return The new stream position, otherwise a negative error value {@link #NOT_CONNECTED},
     * {@link #BACK_PRESSURED}, {@link #ADMIN_ACTION}, or {@link #CLOSED}.
     */
    public long offer(final DirectBuffer buffer, final int offset, final int length)
    {
        return offer(buffer, offset, length, null);
    }

    /**
     * Non-blocking publish of a partial buffer containing a message.
     *
     * @param buffer                containing message.
     * @param offset                offset in the buffer at which the encoded message begins.
     * @param length                in bytes of the encoded message.
     * @param reservedValueSupplier {@link ReservedValueSupplier} for the frame.
     * @return The new stream position, otherwise a negative error value {@link #NOT_CONNECTED},
     * {@link #BACK_PRESSURED}, {@link #ADMIN_ACTION}, or {@link #CLOSED}.
     */
    public long offer(
        final DirectBuffer buffer,
        final int offset,
        final int length,
        final ReservedValueSupplier reservedValueSupplier)
    {
        long newPosition = CLOSED;
        if (!isClosed)
        {
            final long limit = positionLimit.getVolatile();
            final ExclusiveTermAppender termAppender = termAppenders[activePartitionIndex];
            final long position = termBeginPosition + termOffset;

            if (position < limit)
            {
                final int result;
                if (length <= maxPayloadLength)
                {
                    result = termAppender.appendUnfragmentedMessage(
                        termId, termOffset, headerWriter, buffer, offset, length, reservedValueSupplier);
                }
                else
                {
                    checkForMaxMessageLength(length);
                    result = termAppender.appendFragmentedMessage(
                        termId,
                        termOffset,
                        headerWriter,
                        buffer,
                        offset,
                        length,
                        maxPayloadLength,
                        reservedValueSupplier);
                }

                newPosition = newPosition(result);
            }
            else if (conductor.isPublicationConnected(timeOfLastStatusMessage(logMetaDataBuffer)))
            {
                newPosition = BACK_PRESSURED;
            }
            else
            {
                newPosition = NOT_CONNECTED;
            }
        }

        return newPosition;
    }

    /**
     * Try to claim a range in the publication log into which a message can be written with zero copy semantics.
     * Once the message has been written then {@link ExclusiveBufferClaim#commit()} should be called thus making it
     * available.
     * <p>
     * <b>Note:</b> This method can only be used for message lengths less than MTU length minus header.
     * <pre>{@code
     *     final ExclusiveBufferClaim bufferClaim = new ExclusiveBufferClaim();
     *
     *     if (publication.tryClaim(messageLength, bufferClaim) > 0L)
     *     {
     *         try
     *         {
     *              final MutableDirectBuffer buffer = bufferClaim.buffer();
     *              final int offset = bufferClaim.offset();
     *
     *              // Work with buffer directly or wrap with a flyweight
     *         }
     *         finally
     *         {
     *             bufferClaim.commit();
     *         }
     *     }
     * }</pre>
     *
     * @param length      of the range to claim, in bytes..
     * @param bufferClaim to be populated if the claim succeeds.
     * @return The new stream position, otherwise {@link #NOT_CONNECTED}, {@link #BACK_PRESSURED},
     * {@link #ADMIN_ACTION}, or {@link #CLOSED}.
     * @throws IllegalArgumentException if the length is greater than {@link #maxPayloadLength()} within an MTU.
     * @see ExclusiveBufferClaim#commit()
     * @see ExclusiveBufferClaim#abort()
     */
    public long tryClaim(final int length, final ExclusiveBufferClaim bufferClaim)
    {
        checkForMaxPayloadLength(length);
        long newPosition = CLOSED;

        if (!isClosed)
        {
            final long limit = positionLimit.getVolatile();
            final ExclusiveTermAppender termAppender = termAppenders[activePartitionIndex];
            final long position = termBeginPosition + termOffset;

            if (position < limit)
            {
                final int result = termAppender.claim(termId, termOffset, headerWriter, length, bufferClaim);
                newPosition = newPosition(result);
            }
            else if (conductor.isPublicationConnected(timeOfLastStatusMessage(logMetaDataBuffer)))
            {
                newPosition = BACK_PRESSURED;
            }
            else
            {
                newPosition = NOT_CONNECTED;
            }
        }

        return newPosition;
    }

    /**
     * Append a padding record log of a given length to make up the log to a position.
     *
     * @param length of the range to claim, in bytes..
     * @return The new stream position, otherwise {@link #NOT_CONNECTED}, {@link #BACK_PRESSURED},
     * {@link #ADMIN_ACTION}, or {@link #CLOSED}.
     * @throws IllegalArgumentException if the length is greater than {@link #maxMessageLength()}.
     */
    public long appendPadding(final int length)
    {
        checkForMaxMessageLength(length);
        long newPosition = CLOSED;

        if (!isClosed)
        {
            final long limit = positionLimit.getVolatile();
            final ExclusiveTermAppender termAppender = termAppenders[activePartitionIndex];
            final long position = termBeginPosition + termOffset;

            if (position < limit)
            {
                final int result = termAppender.appendPadding(termId, termOffset, headerWriter, length);
                newPosition = newPosition(result);
            }
            else if (conductor.isPublicationConnected(timeOfLastStatusMessage(logMetaDataBuffer)))
            {
                newPosition = BACK_PRESSURED;
            }
            else
            {
                newPosition = NOT_CONNECTED;
            }
        }

        return newPosition;
    }

    /**
     * Add a destination manually to a multi-destination-cast Publication.
     *
     * @param endpointChannel for the destination to add
     */
    public void addDestination(final String endpointChannel)
    {
        conductor.clientLock().lock();
        try
        {
            conductor.addDestination(registrationId, endpointChannel);
        }
        finally
        {
            conductor.clientLock().unlock();
        }
    }

    /**
     * Remove a previously added destination manually from a multi-destination-cast Publication.
     *
     * @param endpointChannel for the destination to remove
     */
    public void removeDestination(final String endpointChannel)
    {
        conductor.clientLock().lock();
        try
        {
            conductor.removeDestination(registrationId, endpointChannel);
        }
        finally
        {
            conductor.clientLock().unlock();
        }
    }

    private long newPosition(final int resultingOffset)
    {
        if (resultingOffset > 0)
        {
            termOffset = resultingOffset;

            return termBeginPosition + resultingOffset;
        }
        else
        {
            final int nextIndex = nextPartitionIndex(activePartitionIndex);
            final int nextTermId = termId + 1;

            activePartitionIndex = nextIndex;
            termOffset = 0;
            termId = nextTermId;
            termBeginPosition = computeTermBeginPosition(nextTermId, positionBitsToShift, initialTermId);

            termAppenders[nextIndex].tailTermId(nextTermId);
            LogBufferDescriptor.activePartitionIndexOrdered(logMetaDataBuffer, nextIndex);

            return ADMIN_ACTION;
        }
    }

    private void checkForMaxPayloadLength(final int length)
    {
        if (length > maxPayloadLength)
        {
            throw new IllegalArgumentException(
                "Claim exceeds maxPayloadLength of " + maxPayloadLength + ", length=" + length);
        }
    }

    private void checkForMaxMessageLength(final int length)
    {
        if (length > maxMessageLength)
        {
            throw new IllegalArgumentException(
                "Message exceeds maxMessageLength of " + maxMessageLength + ", length=" + length);
        }
    }

    ManagedResource managedResource()
    {
        return new PublicationManagedResource();
    }

    private class PublicationManagedResource implements ManagedResource
    {
        private long timeOfLastStateChange = 0;

        public void timeOfLastStateChange(final long time)
        {
            this.timeOfLastStateChange = time;
        }

        public long timeOfLastStateChange()
        {
            return timeOfLastStateChange;
        }

        public void delete()
        {
            logBuffers.close();
        }
    }
}
