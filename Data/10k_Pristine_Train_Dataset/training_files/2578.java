/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.agent.server.exception;

/**
 * Exception that is thrown in case agent start is treated as failed.
 *
 * @author Alexander Garagatyi
 */
public class AgentStartException extends AgentException {
    public AgentStartException(String message) {
        super(message);
    }

    public AgentStartException(String message, Throwable cause) {
        super(message, cause);
    }
}
