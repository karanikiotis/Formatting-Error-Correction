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
package org.eurekastreams.server.action.execution.gallery;

import static org.junit.Assert.assertEquals;

import org.eurekastreams.commons.actions.context.ActionContext;
import org.eurekastreams.commons.actions.context.TaskHandlerActionContext;
import org.eurekastreams.commons.server.UserActionRequest;
import org.eurekastreams.server.domain.GalleryItem;
import org.eurekastreams.server.persistence.GalleryItemMapper;
import org.eurekastreams.server.testing.TestContextCreator;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * Tests HideThenDeleteGadgetExecution.
 */
public class HideGalleryItemThenQueueTaskExecutionTest
{
    /** Test data. */
    private static final String ACTION = "nextAction";

    /** Used for mocking objects. */
    private final JUnit4Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    /** Fixture: mapper. */
    private final GalleryItemMapper<GalleryItem> mapper = context.mock(GalleryItemMapper.class);

    /** Fixture: gadget definition. */
    private final GalleryItem galleryItem = context.mock(GalleryItem.class);

    /**
     * Tests execute.
     */
    @Test
    public void testExecute()
    {
        HideGalleryItemThenQueueTaskExecution sut = new HideGalleryItemThenQueueTaskExecution(mapper, ACTION);

        context.checking(new Expectations()
        {
            {
                allowing(mapper).findById(9L);
                will(returnValue(galleryItem));

                oneOf(galleryItem).setShowInGallery(false);
            }
        });

        TaskHandlerActionContext<ActionContext> wrapperContext = TestContextCreator.createTaskHandlerAsyncContext(9L);

        sut.execute(wrapperContext);

        context.assertIsSatisfied();
        assertEquals(1, wrapperContext.getUserActionRequests().size());
        UserActionRequest asyncRqst = wrapperContext.getUserActionRequests().get(0);
        assertEquals(9L, asyncRqst.getParams());
        assertEquals(ACTION, asyncRqst.getActionKey());
    }
}
