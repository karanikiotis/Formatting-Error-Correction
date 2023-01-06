/*
 * Copyright (c) 2010 Lockheed Martin Corporation
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
package org.eurekastreams.web.client.model;

import org.eurekastreams.server.action.request.profile.GetCurrentUserFollowingStatusRequest;
import org.eurekastreams.server.domain.Follower;
import org.eurekastreams.server.domain.Follower.FollowerStatus;
import org.eurekastreams.web.client.events.data.GotPersonFollowerStatusResponseEvent;
import org.eurekastreams.web.client.ui.Session;

/**
 * The current user's following status.
 *
 */
public class CurrentUserPersonFollowingStatusModel extends BaseModel implements
        Fetchable<GetCurrentUserFollowingStatusRequest>
{

    /**
     * Singleton.
     */
    private static CurrentUserPersonFollowingStatusModel model = new CurrentUserPersonFollowingStatusModel();

    /**
     * Gets the singleton.
     *
     * @return the singleton.
     */
    public static CurrentUserPersonFollowingStatusModel getInstance()
    {
        return model;
    }

    /**
     * {@inheritDoc}
     */
    public void fetch(final GetCurrentUserFollowingStatusRequest request, final boolean useClientCacheIfAvailable)
    {
        super.callReadAction("getCurrentUserFollowingStatus", request, new OnSuccessCommand<Follower.FollowerStatus>()
        {
            public void onSuccess(final FollowerStatus response)
            {
                Session.getInstance().getEventBus().notifyObservers(new GotPersonFollowerStatusResponseEvent(response));
            }
        }, useClientCacheIfAvailable);
    }

}
