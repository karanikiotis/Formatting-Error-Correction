/*
 * Copyright (c) 2010-2012 Lockheed Martin Corporation
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
package org.eurekastreams.server.service.restlets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eurekastreams.commons.actions.context.PrincipalActionContext;
import org.eurekastreams.commons.actions.context.PrincipalPopulator;
import org.eurekastreams.commons.actions.context.service.ServiceActionContext;
import org.eurekastreams.commons.actions.service.ServiceAction;
import org.eurekastreams.commons.server.service.ActionController;
import org.eurekastreams.server.domain.EntityType;
import org.eurekastreams.server.domain.PagedSet;
import org.eurekastreams.server.domain.stream.ActivityDTO;
import org.eurekastreams.server.domain.stream.Stream;
import org.eurekastreams.server.persistence.mappers.FindByIdMapper;
import org.eurekastreams.server.persistence.mappers.requests.FindByIdRequest;
import org.eurekastreams.server.service.restlets.support.RestletQueryRequestParser;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * REST end point for stream filters.
 *
 */
public class StreamXMLResource extends SmpResource
{
    /**
     * Logger.
     */
    private final Log log = LogFactory.getLog(StreamResource.class);

    /**
     * Action.
     */
    private final ServiceAction action;

    /**
     * Service Action Controller.
     */
    private final ActionController serviceActionController;

    /**
     * Principal populator.
     */
    private final PrincipalPopulator principalPopulator;

    /**
     * Used for testing.
     */
    private String pathOverride;

    /**
     * Good status.
     */
    private static final String GOOD_STATUS = "OK";

    /**
     * Stream find by ID mapper.
     */
    private FindByIdMapper<Stream> streamMapper = null;

    /**
     * The mode of the resource, can be ad-hoc query, or saved stream. "query" is for ad-hoc. "saved" is for saved
     * stream.
     */
    private String mode;

    /**
     * The stream Id.
     */
    private long streamId;

    /** Extracts the query out of the request path. */
    private final RestletQueryRequestParser requestParser;
    /**
     * The base url for eurkea.
     */
    private final String baseUrl;

    /**
     * Default constructor.
     *
     * @param inAction
     *            the action.
     * @param inServiceActionController
     *            {@link ActionController} used to execute action.
     * @param inPrincipalPopulator
     *            {@link PrincipalPopulator} used to create principal via open social id.
     * @param inStreamMapper
     *            the stream mapper.
     * @param inRequestParser
     *            Extracts the query out of the request path.
     * @param inBaseUrl
     *            the base url for eureka.
     */
    public StreamXMLResource(final ServiceAction inAction, final ActionController inServiceActionController,
            final PrincipalPopulator inPrincipalPopulator, final FindByIdMapper<Stream> inStreamMapper,
            final RestletQueryRequestParser inRequestParser, final String inBaseUrl)
    {
        action = inAction;
        serviceActionController = inServiceActionController;
        principalPopulator = inPrincipalPopulator;
        streamMapper = inStreamMapper;
        requestParser = inRequestParser;
        baseUrl = inBaseUrl;
    }

    /**
     * init the params.
     *
     * @param request
     *            the request object.
     */
    @Override
    protected void initParams(final Request request)
    {
        mode = (String) request.getAttributes().get("mode");
        String streamIdStr = ((String) request.getAttributes().get("streamId"));

        if (null != streamIdStr && mode.equals("saved"))
        {
            streamId = Long.parseLong(streamIdStr);
        }
    }

    /**
     * GET the activites.
     *
     * @param variant
     *            the variant.
     * @return the ATOM.
     * @throws ResourceException
     *             the exception.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Representation represent(final Variant variant) throws ResourceException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Path: " + getPath());
        }

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");

        String status = GOOD_STATUS;

        try
        {
            JSONObject queryJson = null;

            if (mode.equals("query"))
            {
                queryJson = requestParser.parseRequest(getPath(), 5);

                feed.setTitle("Eureka Activity Stream Feed");
                feed.setLink(baseUrl);

                feed.setDescription("");
            }
            else if (mode.equals("saved"))
            {
                Stream stream = streamMapper.execute(new FindByIdRequest("Stream", streamId));

                String feedTitle = ("Eureka Streams: " + stream.getName());

                feed.setTitle(feedTitle);
                feed.setLink(baseUrl);

                feed.setDescription(feedTitle);

                if (stream == null)
                {
                    throw new Exception("Unknown saved stream.");
                }
                queryJson = JSONObject.fromObject(stream.getRequest());
            }
            else
            {
                throw new Exception("Unknown request mode.");
            }

            // add default date sort if sort not specified.
            JSONObject query = queryJson.getJSONObject("query");
            if (!query.has("sortBy"))
            {
                query.put("sortBy", "date");
            }

            log.debug("Making request using: " + queryJson);

            PrincipalActionContext ac = new ServiceActionContext(queryJson.toString(),
                    principalPopulator.getPrincipal(null, null));

            PagedSet<ActivityDTO> activities = (PagedSet<ActivityDTO>) serviceActionController.execute(ac, action);

            feed.setUri(baseUrl + getRequest().getResourceRef().getPath());

            if (activities.getPagedSet().size() > 0)
            {
                feed.setPublishedDate(activities.getPagedSet().get(0).getPostedTime());
            }
            else
            {
                feed.setPublishedDate(new Date());
            }

            List entries = new ArrayList();

            for (ActivityDTO activity : activities.getPagedSet())
            {
                SyndEntry entry;
                SyndContent description;
                String title = null;

                switch (activity.getBaseObjectType())
                {
                case BOOKMARK:
                    title = activity.getActor().getDisplayName() + ": "
                            + activity.getBaseObjectProperties().get("content") + " "
                            + activity.getBaseObjectProperties().get("targetUrl");
                    break;
                case NOTE:
                    title = activity.getActor().getDisplayName() + ": "
                            + activity.getBaseObjectProperties().get("content");
                    break;
                case PHOTO:
                    title = activity.getActor().getDisplayName() + ": "
                            + activity.getBaseObjectProperties().get("content");
                    break;
                case VIDEO:
                    title = activity.getActor().getDisplayName() + ": "
                            + activity.getBaseObjectProperties().get("content");
                    break;
                default:
                    break;
                }

                if (title != null)
                {
                    title = title.replace("%EUREKA:ACTORNAME%", activity.getActor().getDisplayName());
                    entry = new SyndEntryImpl();
                    entry.setTitle(title);
                    entry.setAuthor(activity.getActor().getDisplayName());
                    String linkPrefix = activity.getDestinationStream().getType() == EntityType.PERSON ? "/#people/"
                            : "/#groups/";
                    String link = baseUrl + linkPrefix + activity.getDestinationStream().getUniqueIdentifier()
                            + "?activityId=" + activity.getId();
                    entry.setLink(link);
                    entry.setUri(link);
                    entry.setPublishedDate(activity.getPostedTime());
                    description = new SyndContentImpl();
                    description.setType("text/plain");
                    description.setValue(title);
                    entry.setDescription(description);
                    entries.add(entry);
                }
            }

            feed.setEntries(entries);

        }
        catch (Exception ex)
        {
            // Log the error, but return an empty feed, so don't throw an error.
            log.error("Error:" + ex.toString());
        }

        log.debug(status);

        SyndFeedOutput output = new SyndFeedOutput();
        StringWriter writer = new StringWriter();

        try
        {
            output.output(feed, writer);
        }
        catch (IOException e)
        {
            log.error("error in writing feed");
        }
        catch (FeedException e)
        {
            log.error("error in feed");
        }

        Representation rep = new StringRepresentation(writer.toString(), MediaType.TEXT_XML);
        rep.setExpirationDate(new Date(0L));

        return rep;
    }

    /**
     * Overrides the request path.
     *
     * @param inPathOverride
     *            the string to override the path with.
     */
    public void setPathOverride(final String inPathOverride)
    {
        pathOverride = inPathOverride;
    }

    /**
     * Get the request path.
     *
     * @return the path.
     */
    public String getPath()
    {
        if (pathOverride == null)
        {
            return getRequest().getResourceRef().getPath();
        }
        else
        {
            return pathOverride;
        }
    }

}
