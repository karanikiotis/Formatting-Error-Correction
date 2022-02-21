/*
 * Copyright 2016 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.manager.api.rest.contract.exceptions;

/**
 * Thrown when the user attempts some action on the Plan when it is
 * not in an appropriate state/status.
 *
 * @author Marc Savy {@literal <msavy@redhat.com>}
 * @author eric.wittmann@redhat.com
 */
public class InvalidPlanStatusException extends AbstractUserException {

    private static final long serialVersionUID = -2327114979102135411L;

    /**
     * Constructor.
     * @param message the exception message
     */
    public InvalidPlanStatusException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLAN_STATUS_ERROR;
    }

    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLAN_STATUS_ERROR_INFO;
    }

}
