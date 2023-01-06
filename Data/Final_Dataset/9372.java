/*
 * Copyright (c) 2009-2010 Lockheed Martin Corporation
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
package org.eurekastreams.server.service.actions.strategies.links;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Test fixture for ConnectionFacade.ImageDimensions.
 */
public class ConnectionFacadeImageDimensionsTest
{
    /**
     * Test the constructor and properties.
     */
    @Test
    public void testProperties()
    {
        final int height = 382;
        final int width = 847;
        ConnectionFacade.ImageDimensions sut = (new ConnectionFacade(new ArrayList<ConnectionFacadeDecorator>()))
            .new ImageDimensions(height, width);
        assertEquals(height, sut.getHeight());
        assertEquals(width, sut.getWidth());
    }
}
