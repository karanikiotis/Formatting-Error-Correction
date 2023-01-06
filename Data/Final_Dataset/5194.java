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
package org.xwiki.refactoring.batch;

/**
 * Describes the work done in a batch operation, executed by a {@link BatchOperationExecutor}.
 *
 * @param <E> the type of exception thrown by the operation
 * @version $Id: cd7a8cb99e7a3eb8e27c7c6760f323e3c73ce8eb $
 * @since 9.5RC1
 */
@FunctionalInterface
public interface BatchOperation<E extends Exception>
{
    /**
     * Do the work.
     *
     * @throws E in case of problems
     */
    void execute() throws E;
}
