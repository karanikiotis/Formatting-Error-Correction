/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.accounts;

/**
 * @version $Id: 238ceeb42db4bd7f3a7f2011d8cfb131ff0bca89 $
 */
public class IncompatibleConnectedUserException extends Exception
{
    public IncompatibleConnectedUserException()
    {
    }

    public IncompatibleConnectedUserException(String message)
    {
        super(message);
    }
}
