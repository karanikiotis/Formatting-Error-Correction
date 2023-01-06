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
package org.xwiki.platform.flavor.job;

import org.xwiki.extension.job.InstallRequest;
import org.xwiki.job.Request;
import org.xwiki.stability.Unstable;

/**
 * Request used in {@link org.xwiki.platform.flavor.internal.job.FlavorSearchJob}.
 *
 * @version $Id: da8aa81b735893fcf27bec5721ba6ca226c30d6d $
 * @since 8.0RC1
 */
@Unstable
public class FlavorSearchRequest extends InstallRequest
{
    /**
     * Default constructor.
     */
    public FlavorSearchRequest()
    {

    }

    /**
     * @param request the request to copy
     */
    public FlavorSearchRequest(Request request)
    {
        super(request);
    }
}
