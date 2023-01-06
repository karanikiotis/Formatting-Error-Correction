/*******************************************************************************
 * Copyright (c) 2009-2013 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available
 * at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.backend.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.erlide.backend.api.IBackend;
import org.erlide.runtime.shell.IBackendShell;
import org.erlide.util.ErlLogger;
import org.erlide.util.IDisposable;

import com.ericsson.otp.erlang.OtpErlangPid;

public class BackendShellManager implements IDisposable {

    private final IBackend backend;
    private final Map<String, BackendShell> fShells;

    public BackendShellManager(final IBackend backend) {
        this.backend = backend;
        fShells = new HashMap<>();
    }

    public BackendShell getShell(final String id) {
        final BackendShell shell = fShells.get(id);
        return shell;
    }

    public synchronized IBackendShell openShell(final String id) {
        BackendShell shell = getShell(id);
        if (shell == null) {
            OtpErlangPid server = null;
            try {
                server = new ErlideReshd().start(backend.getRuntime());
            } catch (final Exception e) {
                ErlLogger.warn(e);
            }
            shell = new BackendShell(backend, id, server);
            shell.open();
            fShells.put(id, shell);
        }
        return shell;
    }

    public synchronized void closeShell(final String id) {
        final IBackendShell shell = getShell(id);
        if (shell != null) {
            fShells.remove(id);
            shell.close();
        }
    }

    @Override
    public void dispose() {
        final Collection<BackendShell> c = fShells.values();
        for (final IBackendShell backendShell : c) {
            backendShell.close();
        }
        fShells.clear();
    }
}
