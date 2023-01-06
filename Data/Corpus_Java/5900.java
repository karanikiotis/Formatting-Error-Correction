package com.firefly.codec.http2.stream;

import com.firefly.codec.http2.frame.DataFrame;
import com.firefly.codec.http2.frame.Frame;
import com.firefly.codec.http2.frame.PushPromiseFrame;
import com.firefly.codec.http2.frame.WindowUpdateFrame;
import com.firefly.utils.concurrent.Callback;
import com.firefly.utils.concurrent.Promise;

/**
 * <p>The SPI interface for implementing a HTTP/2 session.</p>
 * <p>This class extends {@link Session} by adding the methods required to
 * implement the HTTP/2 session functionalities.</p>
 */
public interface SessionSPI extends Session {
    @Override
    public StreamSPI getStream(int streamId);

    /**
     * <p>Removes the given {@code stream}.</p>
     *
     * @param stream the stream to remove
     */
    public void removeStream(StreamSPI stream);

    /**
     * <p>Enqueues the given frames to be written to the connection.</p>
     *
     * @param stream   the stream the frames belong to
     * @param callback the callback that gets notified when the frames have been sent
     * @param frame    the first frame to enqueue
     * @param frames   additional frames to enqueue
     */
    public void frames(StreamSPI stream, Callback callback, Frame frame, Frame... frames);

    /**
     * <p>Enqueues the given PUSH_PROMISE frame to be written to the connection.</p>
     * <p>Differently from {@link #frames(StreamSPI, Callback, Frame, Frame...)}, this method
     * generates atomically the stream id for the pushed stream.</p>
     *
     * @param stream   the stream associated to the pushed stream
     * @param promise  the promise that gets notified of the pushed stream creation
     * @param frame    the PUSH_PROMISE frame to enqueue
     * @param listener the listener that gets notified of pushed stream events
     */
    public void push(StreamSPI stream, Promise<Stream> promise, PushPromiseFrame frame, Stream.Listener listener);

    /**
     * <p>Enqueues the given DATA frame to be written to the connection.</p>
     *
     * @param stream   the stream the data frame belongs to
     * @param callback the callback that gets notified when the frame has been sent
     * @param frame    the DATA frame to send
     */
    public void data(StreamSPI stream, Callback callback, DataFrame frame);

    /**
     * <p>Updates the session send window by the given {@code delta}.</p>
     *
     * @param delta the delta value (positive or negative) to add to the session send window
     * @return the previous value of the session send window
     */
    public int updateSendWindow(int delta);

    /**
     * <p>Updates the session receive window by the given {@code delta}.</p>
     *
     * @param delta the delta value (positive or negative) to add to the session receive window
     * @return the previous value of the session receive window
     */
    public int updateRecvWindow(int delta);

    /**
     * <p>Callback method invoked when a WINDOW_UPDATE frame has been received.</p>
     *
     * @param stream the stream the window update belongs to, or null if the window update belongs to the session
     * @param frame  the WINDOW_UPDATE frame received
     */
    public void onWindowUpdate(StreamSPI stream, WindowUpdateFrame frame);

    /**
     * @return whether the push functionality is enabled
     */
    public boolean isPushEnabled();

    /**
     * <p>Callback invoked when the connection reads -1.</p>
     *
     * @see #onIdleTimeout()
     * @see #close(int, String, Callback)
     */
    public void onShutdown();

    /**
     * <p>Callback invoked when the idle timeout expires.</p>
     *
     * @see #onShutdown()
     * @see #close(int, String, Callback)
     */
    public boolean onIdleTimeout();

    /**
     * <p>Callback method invoked during an HTTP/1.1 to HTTP/2 upgrade requests
     * to process the given synthetic frame.</p>
     *
     * @param frame the synthetic frame to process
     */
    public void onFrame(Frame frame);

    /**
     * @return the number of bytes written by this session
     */
    public long getBytesWritten();
}
