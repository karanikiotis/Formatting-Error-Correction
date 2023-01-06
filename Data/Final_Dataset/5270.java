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
package org.xwiki.eventstream;

import java.util.Set;

import org.xwiki.stability.Unstable;

/**
 * An event that specify which entities are concerned or interested by it.
 *
 * @version $Id: 90188ff5bd608e45ddc1d67006ae209b8ff02af4 $
 * @since 9.2RC1
 */
@Unstable
public interface TargetableEvent extends org.xwiki.observation.event.Event
{
    /**
     * @return the list of users and groups represented by their id that are targeted by this event
     */
    Set<String> getTarget();
}
