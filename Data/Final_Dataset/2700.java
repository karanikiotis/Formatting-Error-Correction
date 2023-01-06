/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.vfs;

/**
 * Encapsulate a VFS error.
 *
 * @version $Id: 62498697360645282429e3ea116618f81729f88d $
 * @since 7.4M2
 */
public class VfsException extends Exception
{
    /**
     * Class ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new Exception with the specified detail message.
     *
     * @param message The detailed message. This can later be retrieved by the Throwable.getMessage() method.
     */
    public VfsException(String message)
    {
        super(message);
    }

    /**
     * Construct a new Exception with the specified detail message and cause.
     *
     * @param message The detailed message. This can later be retrieved by the Throwable.getMessage() method.
     * @param throwable the cause. This can be retrieved later by the Throwable.getCause() method. (A null value is
     *            permitted, and indicates that the cause is nonexistent or unknown)
     */
    public VfsException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    /**
     * Construct a new Exception with the specified detail message and cause.
     *
     * @param message The detailed message using the String.format format.
     * @param throwable the cause. This can be retrieved later by the Throwable.getCause() method. (A null value is
     *            permitted, and indicates that the cause is nonexistent or unknown)
     * @param parameters the parameters to the String.format format
     */
    public VfsException(String message, Throwable throwable, Object... parameters)
    {
        super(String.format(message, parameters), throwable);
    }
}
