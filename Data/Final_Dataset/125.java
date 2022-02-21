package io.digdag.core.session;

import java.util.List;
import com.google.common.base.Optional;
import io.digdag.core.repository.ResourceConflictException;
import io.digdag.core.repository.ResourceNotFoundException;

public interface SessionControlStore
{
    StoredSessionAttempt insertAttempt(long sessionId, int projId, SessionAttempt attempt)
        throws ResourceConflictException, ResourceNotFoundException;

    StoredSessionAttempt insertDelayedAttempt(long sessionId, int projId, SessionAttempt attempt,
            Optional<Long> dependentSessionId)
        throws ResourceConflictException, ResourceNotFoundException;

    StoredSessionAttempt getLastAttempt(long sessionId)
        throws ResourceNotFoundException;

    Optional<StoredSessionAttempt> getLastAttemptIfExists(long sessionId);

    interface SessionBuilderAction <T>
    {
        T call(TaskControlStore store, long rootTaskId);
    }

    <T> T insertRootTask(long attemptId, Task task, SessionBuilderAction<T> func);

    void insertMonitors(long attemptId, List<SessionMonitor> monitors);
}
