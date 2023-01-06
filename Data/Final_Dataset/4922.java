/*
 * Copyright (c) 2009-2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.service.opensocial.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.core.model.ActivityImpl;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.eurekastreams.commons.actions.TaskHandlerAction;
import org.eurekastreams.commons.actions.context.Principal;
import org.eurekastreams.commons.actions.context.service.ServiceActionContext;
import org.eurekastreams.commons.actions.service.ServiceAction;
import org.eurekastreams.commons.actions.service.TaskHandlerServiceAction;
import org.eurekastreams.commons.logging.LogFactory;
import org.eurekastreams.commons.server.service.ActionController;
import org.eurekastreams.server.action.request.opensocial.GetUserActivitiesRequest;
import org.eurekastreams.server.action.request.stream.PostActivityRequest;
import org.eurekastreams.server.domain.EntityType;
import org.eurekastreams.server.domain.GadgetDefinition;
import org.eurekastreams.server.domain.GeneralGadgetDefinition;
import org.eurekastreams.server.domain.gadgetspec.GadgetMetaDataDTO;
import org.eurekastreams.server.domain.stream.ActivityDTO;
import org.eurekastreams.server.domain.stream.ActivityVerb;
import org.eurekastreams.server.domain.stream.BaseObjectType;
import org.eurekastreams.server.domain.stream.StreamEntityDTO;
import org.eurekastreams.server.persistence.DomainEntityMapper;
import org.eurekastreams.server.persistence.GadgetDefinitionMapper;
import org.eurekastreams.server.persistence.mappers.DomainMapper;
import org.eurekastreams.server.service.opensocial.gadgets.spec.GadgetMetaDataFetcher;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class provides the implementation of the ActivityService interface for Shindig.
 */
public class ActivityServiceImpl implements ActivityService
{
    /**
     * Logger.
     */
    private final Log log = LogFactory.make();

    /**
     * Action that gets a specified set of activities for a specified user.
     */
    private final ServiceAction getUserActivitiesAction;

    /**
     * Action that deletes a specified set of activities for a specified user.
     */
    private final TaskHandlerAction deleteUserActivities;

    /**
     * Instance of the {@link ActionController} for this class.
     */
    private final ActionController serviceActionController;

    /**
     * DAO for retrieving {@link Principal} objects given OpenSocial IDs.
     */
    private final DomainMapper<String, Principal> openSocialPrincipalDao;

    /**
     * Instance of the {@link TaskHandlerServiceAction} for this class.
     */
    private final TaskHandlerServiceAction postActivityAction;

    /** For getting gadget definition (in order to get name). */
    private final DomainEntityMapper<GadgetDefinition> gadgetDefinitionMapper;

    /** For getting gadget name. */
    private final GadgetMetaDataFetcher gadgetMetaDataFetcher;

    /**
     * Basic constructor for the PersonService implementation.
     *
     * @param inGetUserActivitiesAction
     *            the action to get a specified set of activities for a specified user.
     * @param inDeleteActivitiesAction
     *            the action to deleted a specified set of activities for a specified user.
     * @param inServiceActionController
     *            - instance of the {@link ActionController} used to execute the actions.
     * @param inOpenSocialPrincipalDao
     *            DAO for retrieving {@link Principal} objects given OpenSocial IDs.
     * @param inPostActivityAction
     *            the action to create an activity.
     * @param inGadgetDefinitionMapper
     *            Mapper to fetch gadget definitions (for looking up title).
     * @param inGadgetMetaDataFetcher
     *            Used to fetch gadget metadata (for looking up title).
     */
    @Inject
    public ActivityServiceImpl(@Named("getUserActivities") final ServiceAction inGetUserActivitiesAction,
            @Named("deleteUserActivities") final TaskHandlerAction inDeleteActivitiesAction,
            final ActionController inServiceActionController,
            @Named("openSocialPrincipalDao") final DomainMapper<String, Principal> inOpenSocialPrincipalDao,
            @Named("postPersonActivityServiceActionTaskHandler") final TaskHandlerServiceAction inPostActivityAction,
            @Named("jpaGadgetDefinitionMapper") final GadgetDefinitionMapper inGadgetDefinitionMapper,
            final GadgetMetaDataFetcher inGadgetMetaDataFetcher)
    {
        getUserActivitiesAction = inGetUserActivitiesAction;
        deleteUserActivities = inDeleteActivitiesAction;
        serviceActionController = inServiceActionController;
        openSocialPrincipalDao = inOpenSocialPrincipalDao;
        postActivityAction = inPostActivityAction;
        gadgetDefinitionMapper = inGadgetDefinitionMapper;
        gadgetMetaDataFetcher = inGadgetMetaDataFetcher;
    }

    /**
     * Create Activity Implementation for Shindig.
     *
     * @param userId
     *            - id of the user to create the activity for.
     * @param groupId
     *            - id of the group that the user belongs to.
     * @param appId
     *            - id of the application creating the activity.
     * @param fields
     *            - //TODO not sure about this one yet.
     * @param activity
     *            - the activity to be added.
     * @param token
     *            - the security token for the request.
     *
     * @return void
     */
    public Future<Void> createActivity(final UserId userId, final GroupId groupId, final String appId,
            final Set<String> fields, final Activity activity, final SecurityToken token)
    {
        log.debug("Entering createActivity data with userId " + userId.getUserId(token) + ", appId " + appId + ", "
                + fields.size() + ", AcivityId " + activity.getId() + ", token appId " + token.getAppId());
        try
        {
            Principal currentUserPrincipal = openSocialPrincipalDao.execute(userId.getUserId(token));

            // Create the actor.
            StreamEntityDTO actorEntity = new StreamEntityDTO();
            actorEntity.setUniqueIdentifier(currentUserPrincipal.getOpenSocialId());
            actorEntity.setType(EntityType.PERSON);

            // Create the destination stream.
            StreamEntityDTO destStream = new StreamEntityDTO();
            destStream.setUniqueIdentifier(currentUserPrincipal.getAccountId());
            destStream.setType(EntityType.PERSON);

            // Create the activitydto object.
            ActivityDTO currentActivity = new ActivityDTO();
            currentActivity.setActor(actorEntity);
            currentActivity.setBaseObjectProperties(new HashMap<String, String>(
                    convertActivityFromOSToEureka(activity)));
            Long appIdNumeric = Long.valueOf(appId);
            currentActivity.setAppId(appIdNumeric);
            currentActivity.setAppType(EntityType.APPLICATION);
            currentActivity.setAppName(lookupGadgetTitle(appIdNumeric));

            // Sets default activity type
            if (!currentActivity.getBaseObjectProperties().containsKey("baseObjectType"))
            {
                currentActivity.setBaseObjectType(BaseObjectType.NOTE);
            }
            else
            {
                String objectType = currentActivity.getBaseObjectProperties().get("baseObjectType");
                currentActivity.setBaseObjectType(BaseObjectType.valueOf(objectType));

                if (currentActivity.getBaseObjectProperties().containsKey("source"))
                {
                    currentActivity.setAppSource(currentActivity.getBaseObjectProperties().get("source"));
                }
            }

            currentActivity.setVerb(ActivityVerb.POST);
            currentActivity.setDestinationStream(destStream);

            PostActivityRequest params = new PostActivityRequest(currentActivity);
            ServiceActionContext currentContext = new ServiceActionContext(params, currentUserPrincipal);
            // Make the call to the action to perform the create.
            serviceActionController.execute(currentContext, postActivityAction);
        }
        catch (Exception e)
        {
            log.error("Error occurred creating Activity ", e);
            throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }

        return ImmediateFuture.newInstance(null);
    }

    /**
     * Retrieves the gadget's title given it's ID.
     *
     * @param appId
     *            Gadget ID.
     * @return Title.
     * @throws Exception
     *             On error.
     */
    private String lookupGadgetTitle(final Long appId) throws Exception
    {
        // TODO: This is the brute-force approach. It gets the gadget definition from the database, then uses the gadget
        // metadata fetcher which does a whole bunch of stuff that we don't need (e.g. user prefs, etc.) -- all just so
        // we can get the title. Note that we should not use the title from the gadget definition even though it is
        // there - it is deprecated.
        GadgetDefinition gadgetDef = gadgetDefinitionMapper.findById(appId);
        List<GadgetMetaDataDTO> gadgetsMetadata = gadgetMetaDataFetcher.getGadgetsMetaData(Collections.singletonMap(
                gadgetDef.getUrl(), (GeneralGadgetDefinition) gadgetDef));
        return gadgetsMetadata.get(0).getTitle();
    }

    /**
     * Delete activities implementation for Shindig.
     *
     * @param userId
     *            - id of the user to delete the activities for.
     * @param groupId
     *            - id of the group the user is in.
     * @param appId
     *            - id of the application deleting the activities.
     * @param activityIds
     *            - set of ids to be deleted.
     * @param token
     *            - the security token for the request.
     *
     * @return void
     */
    public Future<Void> deleteActivities(final UserId userId, final GroupId groupId, final String appId,
            final Set<String> activityIds, final SecurityToken token)
    {
        log.debug("Entering deleteActivities data with userId" + userId.getUserId(token) + ", appId " + appId + ", "
                + activityIds.size() + ", token appId " + token.getAppId());

        Principal currentUserPrincipal = openSocialPrincipalDao.execute(userId.getUserId(token));

        try
        {
            // convert set of string to list of longs to call action with.
            ArrayList<Long> params = new ArrayList<Long>(activityIds.size());
            for (String id : activityIds)
            {
                params.add(Long.parseLong(id));
            }

            // create the action context.
            ServiceActionContext currentContext = new ServiceActionContext(params, currentUserPrincipal);

            // execute the action.
            serviceActionController.execute(currentContext, deleteUserActivities);
        }
        catch (Exception e)
        {
            log.error("Error occurred deleting OpenSocial Application Data " + e.toString());

            throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
        return ImmediateFuture.newInstance(null);
    }

    /**
     * Shindig implementation for retrieving activities from a set of users.
     *
     * @param userIds
     *            - set of userIds to retrieve activities for.
     * @param groupId
     *            - //TODO not sure about this one yet.
     * @param appId
     *            - id of the application requesting the activities.
     * @param fields
     *            - set of fields to retrieve.
     * @param options
     *            - collection of options for retrieving activities.
     * @param token
     *            - the security token for the request.
     *
     * @return collection of activities.
     */
    @SuppressWarnings("unchecked")
    public Future<RestfulCollection<Activity>> getActivities(final Set<UserId> userIds, final GroupId groupId,
            final String appId, final Set<String> fields, final CollectionOptions options, final SecurityToken token)
    {
        log.trace("Entering getActivities");

        List<Activity> osActivities = new ArrayList<Activity>();

        try
        {
            Set<String> userIdList = new HashSet<String>();
            for (UserId currentUserId : userIds)
            {
                if (!currentUserId.getUserId(token).equals("null"))
                {
                    userIdList.add(currentUserId.getUserId(token));
                }
            }

            log.debug("Sending getActivities userIdList to action: " + userIdList.toString());

            GetUserActivitiesRequest currentRequest = new GetUserActivitiesRequest(new ArrayList<Long>(), userIdList);
            ServiceActionContext currentContext = new ServiceActionContext(currentRequest,
                    openSocialPrincipalDao.execute(token.getViewerId()));

            LinkedList<ActivityDTO> activities = (LinkedList<ActivityDTO>) serviceActionController.execute(
                    currentContext, getUserActivitiesAction);

            log.debug("Retrieved " + activities.size() + " activities from action");

            for (ActivityDTO currentActivity : activities)
            {
                osActivities.add(convertActivityFromEurekaActivityDTOToOS(currentActivity));
            }
        }
        catch (Exception ex)
        {
            log.error("Error occurred retrieving activities ", ex);
            throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return ImmediateFuture.newInstance(new RestfulCollection<Activity>(osActivities));
    }

    /**
     * Shindig implementation for retrieving activities from a single user.
     *
     * @param userId
     *            - id of the user to retrieve the activities for.
     * @param groupId
     *            - id of the group that the user is a member of.
     * @param appId
     *            - id of the application requesting the activities.
     * @param fields
     *            - set of fields to retrieve for the activity.
     * @param options
     *            - collection of options for retrieving the activities.
     * @param activityIds
     *            - set of ids of the activities to retrieve.
     * @param token
     *            - the security token for the request.
     *
     * @return collection of activities.
     */
    @SuppressWarnings("unchecked")
    public Future<RestfulCollection<Activity>> getActivities(final UserId userId, final GroupId groupId,
            final String appId, final Set<String> fields, final CollectionOptions options,
            final Set<String> activityIds, final SecurityToken token)
    {
        log.trace("Entering getActivities");
        List<Activity> osActivities = new ArrayList<Activity>();
        try
        {
            log.debug("Sending getActivities activityIdList to action: " + activityIds.toString());

            LinkedList<Long> activityIdsForRequest = new LinkedList<Long>();
            for (String currentActivityId : activityIds)
            {
                activityIdsForRequest.add(new Long(currentActivityId));
            }

            Set<String> openSocialIdsForRequest = new HashSet<String>();
            openSocialIdsForRequest.add(userId.getUserId(token));

            GetUserActivitiesRequest currentRequest = new GetUserActivitiesRequest(activityIdsForRequest,
                    openSocialIdsForRequest);
            ServiceActionContext currentContext = new ServiceActionContext(currentRequest,
                    openSocialPrincipalDao.execute(userId.getUserId(token)));

            LinkedList<ActivityDTO> activities = (LinkedList<ActivityDTO>) serviceActionController.execute(
                    currentContext, getUserActivitiesAction);

            log.debug("Retrieved " + activities.size() + " activities from action");

            for (ActivityDTO currentActivity : activities)
            {
                osActivities.add(convertActivityFromEurekaActivityDTOToOS(currentActivity));
            }
        }
        catch (Exception ex)
        {
            log.error("Error occurred retrieving activities ", ex);
            throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return ImmediateFuture.newInstance(new RestfulCollection<Activity>(osActivities));
    }

    /**
     * Shindig implementation for retrieving a single activity.
     *
     * @param userId
     *            - id of the user to retrieve the activity for.
     * @param groupId
     *            - id of the group that the user belongs to.
     * @param appId
     *            - id of the application requesting the activity.
     * @param fields
     *            - //TODO not sure about this yet.
     * @param activityId
     *            - id of the activity to retrieve.
     * @param token
     *            - the security token for the request.
     *
     * @return single Activity
     */
    public Future<Activity> getActivity(final UserId userId, final GroupId groupId, final String appId,
            final Set<String> fields, final String activityId, final SecurityToken token)
    {
        // put the activity id in a list and call the getActivities method
        Set<String> activityIds = new HashSet<String>();
        activityIds.add(activityId);

        Future<RestfulCollection<Activity>> activities = getActivities(userId, groupId, appId, fields, null,
                activityIds, token);

        // pull the returned activitity out of the list and return it to the client
        Future<Activity> outActivity = null;
        try
        {
            outActivity = ImmediateFuture.newInstance(activities.get().getEntry().get(0));
        }
        catch (Exception e)
        {
            log.error(e);
        }

        return outActivity;
    }

    /**
     * Helper method that converts a passed in eurekastreams ActivityDTO object into a Shindig Activity object.
     *
     * @param inEurekaActivityDTO
     *            - eurekastreams ActivityDTO to be converted.
     * @return converted OpenSocial Activity object.
     */
    private Activity convertActivityFromEurekaActivityDTOToOS(final ActivityDTO inEurekaActivityDTO)
    {
        Activity osActivity = new ActivityImpl();

        // TODO: this code needs to be refactor when new message object rearch is done.
        // Populate the OpenSocial Activity properties.
        // osActivity.setAppId(inEurekaActivity.getAppId());
        // osActivity.setBody(inEurekaActivity.getBody());
        // osActivity.setBodyId(inEurekaActivity.getBodyId());
        osActivity.setExternalId(String.valueOf(inEurekaActivityDTO.getId()));
        // osActivity.setId(String.valueOf(inEurekaActivityDTO.getEntityId()));
        osActivity.setUpdated(inEurekaActivityDTO.getPostedTime());
        // osActivity.setPriority(inEurekaActivity.getPriority());
        // osActivity.setStreamFaviconUrl(inEurekaActivity.getStreamFaviconUrl());
        // osActivity.setStreamSourceUrl(inEurekaActivity.getStreamSourceUrl());
        osActivity.setStreamTitle(inEurekaActivityDTO.getDestinationStream().getDisplayName());
        // osActivity.setStreamUrl(inEurekaActivity.getStreamUrl());
        // osActivity.setTemplateParams(inEurekaActivity.getTemplateParams());
        osActivity.setTitle(inEurekaActivityDTO.getBaseObjectProperties().get("Content"));
        // osActivity.setTitleId(inEurekaActivity.getTitleId());
        // osActivity.setUrl(inEurekaActivity.getUrl());
        osActivity.setUserId(inEurekaActivityDTO.getActor().getUniqueIdentifier());

        return osActivity;
    }

    /**
     * Helper method that converts a passed in eurekastreams Activity object into a Shindig Activity object.
     *
     * @param inOSActivity
     *            - eurekastreams Activity to be converted.
     * @return converted Activity object.
     */
    private HashMap<String, String> convertActivityFromOSToEureka(final Activity inOSActivity)
    {
        HashMap<String, String> outMap = new HashMap<String, String>();

        outMap.put("appId", inOSActivity.getAppId());
        if (inOSActivity.getBody() == null)
        {
            outMap.put("body", "body");
        }
        else
        {
            outMap.put("body", inOSActivity.getBody());
        }
        outMap.put("bodyId", inOSActivity.getBodyId());
        outMap.put("id", inOSActivity.getExternalId());
        outMap.put("openSocialId", inOSActivity.getId());
        if (inOSActivity.getUpdated() != null)
        {
            outMap.put("updated", inOSActivity.getUpdated().toString());
        }
        if (inOSActivity.getPostedTime() != null)
        {
            outMap.put("postedTime", inOSActivity.getPostedTime().toString());
        }

        if (inOSActivity.getPriority() == null)
        {
            outMap.put("priority", Float.valueOf(0).toString());
        }
        else
        {
            outMap.put("priority", inOSActivity.getPriority().toString());
        }
        outMap.put("streamFaviconUrl", inOSActivity.getStreamFaviconUrl());
        outMap.put("streamSourceUrl", inOSActivity.getStreamSourceUrl());
        outMap.put("streamTitle", inOSActivity.getStreamTitle());
        outMap.put("streamUrl", inOSActivity.getStreamUrl());

        if (inOSActivity.getTemplateParams() != null)
        {
            for (Entry<String, String> currentEntry : inOSActivity.getTemplateParams().entrySet())
            {
                outMap.put(currentEntry.getKey(), currentEntry.getValue());
            }
        }

        if (inOSActivity.getTitle() != null && inOSActivity.getTitle().length() > 0)
        {
            outMap.put("content", inOSActivity.getTitle());
        }
        else
        {
            outMap.put("content", inOSActivity.getTitleId());
        }
        outMap.put("url", inOSActivity.getUrl());
        outMap.put("userId", inOSActivity.getUserId());

        return outMap;
    }
}
