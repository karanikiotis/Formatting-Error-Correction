/*
 * Copyright (c) 2010-2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.action.execution.stream;

import static org.eurekastreams.commons.test.IsEqualInternally.equalInternally;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONObject;

import org.eurekastreams.server.domain.stream.ActivityDTO;
import org.eurekastreams.server.persistence.mappers.DomainMapper;
import org.eurekastreams.server.search.modelview.PersonModelView;
import org.eurekastreams.server.service.actions.strategies.activity.ActivityFilter;
import org.eurekastreams.server.service.actions.strategies.activity.ListCollider;
import org.eurekastreams.server.service.actions.strategies.activity.datasources.DescendingOrderDataSource;
import org.eurekastreams.server.service.actions.strategies.activity.datasources.SortedDataSource;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for GetActivityIdsByJson class.
 *
 */
public class GetActivityIdsByJsonTest
{
    /**
     * Context for building mock objects.
     */
    private final Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    /**
     * System under test.
     */
    private GetActivityIdsByJson sut;

    /**
     * Person id.
     */
    private final Long personId = 82377L;

    /**
     * Filter Mock.
     */
    private final ActivityFilter filterMock = context.mock(ActivityFilter.class);

    /**
     * List of all activity ids.
     */
    private final ArrayList<Long> allActivityIds = new ArrayList<Long>();

    /**
     * Memcache data source.
     */
    private final DescendingOrderDataSource memcacheDS = context.mock(DescendingOrderDataSource.class, "memcache");

    /**
     * Lucene Data source.
     */
    private final SortedDataSource luceneDS = context.mock(SortedDataSource.class, "lucene");

    /** Factory for a trimmer that doesn't apply. */
    private final ActivityQueryListTrimmerFactory nonApplicableTrimmerFactory = context.mock(
            ActivityQueryListTrimmerFactory.class, "nonApplicableTrimmerFactory");

    /**
     * Security mapper.
     */
    private final ActivitySecurityTrimmer securityTrimmer = context.mock(ActivitySecurityTrimmer.class);

    /**
     * AND Collider.
     */
    private final ListCollider andCollider = context.mock(ListCollider.class);

    /**
     * People mapper.
     */
    private final DomainMapper<List<Long>, List<PersonModelView>> peopleMapper = context.mock(DomainMapper.class);

    /**
     * Replace string.
     */
    private final String replaceString = "USER_ID";

    /**
     * 10.
     */
    private static final int THENUMBERTEN = 10;

    /**
     * 19+1. 120%100 pi+16.8584073....
     */
    private static final int THENUMBERTWENTY = 20;

    /**
     * Setup text fixtures.
     */
    @Before
    public final void setUp()
    {
        List<ActivityFilter> filters = new LinkedList<ActivityFilter>();
        filters.add(filterMock);

        sut = new GetActivityIdsByJson(memcacheDS, luceneDS, andCollider, Arrays.asList(nonApplicableTrimmerFactory,
                securityTrimmer), peopleMapper, replaceString);

        // create the activity ids list
        allActivityIds.add(9L);
        allActivityIds.add(8L);
        allActivityIds.add(7L);
        allActivityIds.add(6L);
        allActivityIds.add(5L);
        allActivityIds.add(4L);
        allActivityIds.add(3L);
        allActivityIds.add(2L);
        allActivityIds.add(1L);

        context.checking(new Expectations()
        {
            {
                oneOf(securityTrimmer).getTrimmer(with(any(JSONObject.class)), with(equal(personId)));
                will(returnValue(securityTrimmer));

                oneOf(nonApplicableTrimmerFactory).getTrimmer(with(any(JSONObject.class)), with(equal(personId)));
                will(returnValue(null));
            }
        });
    }

    /**
     * Perform action test with one item in the list.
     *
     * @throws Exception
     *             on failure.
     */
    @Test
    public final void performActionTest() throws Exception
    {
        final String request = "{count:10}";

        context.checking(new Expectations()
        {
            {
                ArrayList<Long> memcacheIds = new ArrayList<Long>();
                memcacheIds.add(2L);

                ArrayList<Long> luceneIds = new ArrayList<Long>();

                ActivityDTO dto = new ActivityDTO();
                dto.setId(3);
                dto.setPostedTime(new Date());
                dto.setIsDestinationStreamPublic(true);

                ArrayList<Long> combinedIds = new ArrayList<Long>();
                combinedIds.add(2L);

                oneOf(memcacheDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(memcacheIds));

                oneOf(luceneDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(luceneIds));

                oneOf(andCollider).collide(with(equalInternally(memcacheIds)), with(equalInternally(luceneIds)),
                        with(equalInternally(THENUMBERTEN * 2)));
                will(returnValue(combinedIds));

                oneOf(securityTrimmer).trim(combinedIds, personId);
                will(returnValue(combinedIds));
            }
        });

        List<Long> results = sut.execute(request, personId);

        context.assertIsSatisfied();
        assertEquals(1, results.size());
    }

    /**
     * Perform action test with one item in the list. Tests with a replace done for the user string.
     *
     * @throws Exception
     *             on failure.
     */
    @Test
    public final void performActionWithReplace() throws Exception
    {
        final String request = "{count:10, query : { followedBy: \"" + replaceString + "\"}}";

        final PersonModelView person = context.mock(PersonModelView.class);
        final List<Long> peopleIds = new ArrayList<Long>();
        peopleIds.add(personId);
        final List<PersonModelView> people = new ArrayList<PersonModelView>();
        people.add(person);

        context.checking(new Expectations()
        {
            {
                ArrayList<Long> memcacheIds = new ArrayList<Long>();
                memcacheIds.add(2L);

                ArrayList<Long> luceneIds = new ArrayList<Long>();

                ActivityDTO dto = new ActivityDTO();
                dto.setId(3);
                dto.setPostedTime(new Date());
                dto.setIsDestinationStreamPublic(true);

                ArrayList<Long> combinedIds = new ArrayList<Long>();
                combinedIds.add(2L);

                oneOf(memcacheDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(memcacheIds));

                oneOf(luceneDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(luceneIds));

                oneOf(peopleMapper).execute(peopleIds);
                will(returnValue(people));

                oneOf(person).getAccountId();
                will(returnValue("acctid"));

                oneOf(andCollider).collide(with(equalInternally(memcacheIds)), with(equalInternally(luceneIds)),
                        with(equalInternally(THENUMBERTEN * 2)));
                will(returnValue(combinedIds));

                oneOf(securityTrimmer).trim(combinedIds, personId);
                will(returnValue(combinedIds));
            }
        });

        List<Long> results = sut.execute(request, personId);

        context.assertIsSatisfied();
        assertEquals(1, results.size());
    }

    /**
     * Test executing a page of data where the user needs three batches to get a full page of activities he can see.
     */
    @Test
    public void testPerformActionRequiringMultipleBatches()
    {
        final String request = "{ \"count\": 2, \"maxId\": 2817 }";

        final ArrayList<Long> activityIdsFirstPass = new ArrayList<Long>();
        activityIdsFirstPass.add(allActivityIds.get(0)); // 9 // 2 * 2^1 = 4
        activityIdsFirstPass.add(allActivityIds.get(1)); // 8
        activityIdsFirstPass.add(allActivityIds.get(2)); // 7
        activityIdsFirstPass.add(allActivityIds.get(3)); // 6

        final ArrayList<Long> activityIdsFirstPassNewItems = new ArrayList<Long>();
        activityIdsFirstPassNewItems.add(allActivityIds.get(0)); // 9
        activityIdsFirstPassNewItems.add(allActivityIds.get(1)); // 8
        activityIdsFirstPassNewItems.add(allActivityIds.get(2)); // 7
        activityIdsFirstPassNewItems.add(allActivityIds.get(3)); // 6

        final ArrayList<Long> activityIdsSecondPass = new ArrayList<Long>();
        activityIdsSecondPass.add(allActivityIds.get(0)); // 9 // 2 * 2^2 + 1 = 9
        activityIdsSecondPass.add(allActivityIds.get(1)); // 8
        activityIdsSecondPass.add(allActivityIds.get(2)); // 7
        activityIdsSecondPass.add(allActivityIds.get(3)); // 6
        activityIdsSecondPass.add(allActivityIds.get(4)); // 5
        activityIdsSecondPass.add(allActivityIds.get(5)); // 4
        activityIdsSecondPass.add(allActivityIds.get(6)); // 3
        activityIdsSecondPass.add(allActivityIds.get(7)); // 2

        final ArrayList<Long> activityIdsSecondPassNewItems = new ArrayList<Long>();
        activityIdsSecondPassNewItems.add(allActivityIds.get(4)); // 5
        activityIdsSecondPassNewItems.add(allActivityIds.get(5)); // 4
        activityIdsSecondPassNewItems.add(allActivityIds.get(6)); // 3
        activityIdsSecondPassNewItems.add(allActivityIds.get(7)); // 2

        final ArrayList<Long> activityIdsThirdPass = new ArrayList<Long>();
        activityIdsThirdPass.add(allActivityIds.get(0)); // 9 // 2 * 2^3 +1 = 17 (we only have 9)
        activityIdsThirdPass.add(allActivityIds.get(1)); // 8
        activityIdsThirdPass.add(allActivityIds.get(2)); // 7
        activityIdsThirdPass.add(allActivityIds.get(3)); // 6
        activityIdsThirdPass.add(allActivityIds.get(4)); // 5
        activityIdsThirdPass.add(allActivityIds.get(5)); // 4
        activityIdsThirdPass.add(allActivityIds.get(6)); // 3
        activityIdsThirdPass.add(allActivityIds.get(7)); // 2
        activityIdsThirdPass.add(allActivityIds.get(8)); // 1 // ran out

        final ArrayList<Long> activityIdsThirdPassNewItems = new ArrayList<Long>();
        activityIdsThirdPassNewItems.add(allActivityIds.get(8)); // 1

        context.checking(new Expectations()
        {
            {
                allowing(luceneDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(null));

                // first pass
                oneOf(memcacheDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(activityIdsFirstPass));

                oneOf(securityTrimmer).trim(activityIdsFirstPassNewItems, personId);
                will(returnValue(Arrays.asList(9L)));

                // second pass
                oneOf(memcacheDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(activityIdsSecondPass));

                oneOf(securityTrimmer).trim(activityIdsSecondPassNewItems, personId);
                will(returnValue(new ArrayList<Long>()));

                // third pass
                oneOf(memcacheDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(activityIdsThirdPass));

                oneOf(securityTrimmer).trim(activityIdsThirdPassNewItems, personId);
                will(returnValue(Arrays.asList(1L)));
            }
        });

        List<Long> results = sut.execute(request, personId);

        // we're asking for 2 results - we should have gotten back 1 and 7
        assertEquals(2, results.size());
        assertSame(9L, results.get(0));
        assertSame(1L, results.get(1));

        context.assertIsSatisfied();
    }

    /**
     * Test executing a page of data where the user needs two batches to get a full page of activities he can see.
     */
    @Test
    public void testPerformActionWithPrivateActivitiesUserCanSee()
    {
        final String request = "{ \"count\": 3, \"maxId\": 7 }";

        final ArrayList<Long> activityIds = new ArrayList<Long>();
        activityIds.add(allActivityIds.get(3));
        activityIds.add(allActivityIds.get(4));
        activityIds.add(allActivityIds.get(5));

        context.checking(new Expectations()
        {
            {

                allowing(luceneDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(null));

                oneOf(memcacheDS).fetch(with(any(JSONObject.class)), with(any(Long.class)));
                will(returnValue(activityIds));

                oneOf(securityTrimmer).trim(activityIds, personId);
                will(returnValue(activityIds));
            }
        });

        List<Long> results = sut.execute(request, personId);

        // we're asking for 2 results - we should have gotten back 1 and 7
        assertEquals(3, results.size());
        assertSame(6L, results.get(0));
        assertSame(5L, results.get(1));
        assertSame(4L, results.get(2));

        context.assertIsSatisfied();
    }

}
