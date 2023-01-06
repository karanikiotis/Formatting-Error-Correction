/*
 * Copyright (c) 2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.action.execution;

import java.io.Serializable;

import org.eurekastreams.server.domain.dto.StreamDiscoverListsDTO;
import org.eurekastreams.server.persistence.mappers.DomainMapper;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * Test fixture for RegenerateStreamDiscoverListsExecution.
 */
public class RegenerateStreamDiscoverListsExecutionTest
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
     * Mapper to get the stream discover lists mapper and force a cache refresh.
     */
    private DomainMapper<Serializable, StreamDiscoverListsDTO> streamDiscoverListsMapper = context.mock(
            DomainMapper.class, "streamDiscoverListsMapper");

    /**
     * System under test.
     */
    private RegenerateStreamDiscoverListsExecution sut = new RegenerateStreamDiscoverListsExecution(
            streamDiscoverListsMapper);

    /**
     * Test execute.
     */
    @Test
    public void testExecute()
    {
        context.checking(new Expectations()
        {
            {
                oneOf(streamDiscoverListsMapper).execute(null);
            }
        });

        sut.execute(null);
        context.assertIsSatisfied();
    }
}
