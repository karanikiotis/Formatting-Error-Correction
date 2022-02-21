package io.qdb.server;

import io.qdb.server.input.InputManager;
import io.qdb.server.input.InputStatusMonitor;
import io.qdb.server.output.OutputManager;
import io.qdb.server.output.OutputStatusMonitor;
import io.qdb.server.queue.QueueManager;
import io.qdb.server.queue.QueueStatusMonitor;
import org.simpleframework.transport.connect.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Closeable;
import java.io.IOException;

/**
 * Manages shutdown of the server.
 */
@Singleton
public class ShutdownManager implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(ShutdownManager.class);

    private final Connection connection;
    private final OutputManager outputManager;
    private final InputManager inputManager;
    private final QueueManager queueManager;
    private final QueueStatusMonitor queueStatusMonitor;
    private final OutputStatusMonitor outputStatusMonitor;
    private final InputStatusMonitor inputStatusMonitor;

    @Inject
    public ShutdownManager(Connection connection, OutputManager outputManager, InputManager inputManager,
                           QueueManager queueManager, QueueStatusMonitor queueStatusMonitor,
                           OutputStatusMonitor outputStatusMonitor, InputStatusMonitor inputStatusMonitor) {
        this.connection = connection;
        this.outputManager = outputManager;
        this.inputManager = inputManager;
        this.queueManager = queueManager;
        this.queueStatusMonitor = queueStatusMonitor;
        this.outputStatusMonitor = outputStatusMonitor;
        this.inputStatusMonitor = inputStatusMonitor;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            log.error("Error closing listener: " + e, e);
        }
        queueStatusMonitor.close();
        outputStatusMonitor.close();
        inputStatusMonitor.close();
        try {
            outputManager.close();
        } catch (Exception e) {
            log.error("Error closing output manager: " + e, e);
        }
        try {
            inputManager.close();
        } catch (Exception e) {
            log.error("Error closing input manager: " + e, e);
        }
        try {
            queueManager.close();
        } catch (Exception e) {
            log.error("Error closing queue manager: " + e, e);
        }
        log.info("Shutdown complete");
    }
}
