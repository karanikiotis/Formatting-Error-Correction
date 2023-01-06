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
package com.xpn.xwiki.internal;

import org.xwiki.job.AbstractJobStatus;
import org.xwiki.logging.LoggerManager;
import org.xwiki.observation.ObservationManager;

/**
 * Used to expose {@link WikiInitializerJob} status.
 *
 * @version $Id: e80d670aa174c15c7f7055253eaa6761b163692a $
 * @since 8.4RC1
 */
public class WikiInitializerJobStatus extends AbstractJobStatus<WikiInitializerRequest>
{
    /**
     * @param request the request provided when started the job
     * @param observationManager the observation manager component
     * @param loggerManager the logger manager component
     */
    public WikiInitializerJobStatus(WikiInitializerRequest request, ObservationManager observationManager,
        LoggerManager loggerManager)
    {
        super(WikiInitializerJob.JOBTYPE, request, null, observationManager, loggerManager);

        setIsolated(false);
    }
}
