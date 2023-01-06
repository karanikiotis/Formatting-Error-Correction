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
package org.eurekastreams.server.persistence.mappers.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.eurekastreams.server.domain.stream.BaseObjectType;
import org.junit.Test;

/**
 * Test fixture for ActivityContentExtractor.
 */
public class ActivityContentExtractorTest
{
    /**
     * System under test.
     */
    private final ActivityContentExtractor sut = new ActivityContentExtractor();

    /**
     * Description.
     */
    private final String description = "sdlkfjsdlkjfsdjfsdkfsdfklsdjfsdjf";

    /**
     * Content.
     */
    private final String content = "sldkjf lkjsdf sldkjf sldkfj sdlfkj sdfjkl";

    /**
     * Title.
     */
    private final String targetTitle = "38skjsdlkj 2likjsd";

    /**
     * Test when there is no base object.
     */
    @Test
    public void testExtractContentUnsupportedType()
    {
        assertNull(sut.extractContent(BaseObjectType.VIDEO, new HashMap<String, String>()));
    }

    /**
     * Test when there is no base object.
     */
    @Test
    public void testExtractContentWithNoBaseObject()
    {
        assertNull(sut.extractContent(BaseObjectType.NOTE, null));
    }

    /**
     * Test objectToString() when base object type is Note and there is content.
     */
    @Test
    public void testObjectToStringFromActivityNote()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("content", content);

        assertEquals(content, sut.extractContent(BaseObjectType.NOTE, baseObject));
    }

    /**
     * Test when base object type is Note and there is no content.
     */
    @Test
    public void testObjectToStringFromActivityNoteWithNoContent()
    {
        assertNull(sut.extractContent(BaseObjectType.NOTE, new HashMap<String, String>()));
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is content, description, and title.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithContentAndDescriptionAndTitle()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("content", content);
        baseObject.put("description", description);
        baseObject.put("targetTitle", targetTitle);

        assertEquals(content + " " + targetTitle + " " + description, sut.extractContent(BaseObjectType.BOOKMARK,
                baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is content and description, but no title.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithContentAndDescription()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("content", content);
        baseObject.put("description", description);

        assertEquals(content + " " + description, sut.extractContent(BaseObjectType.BOOKMARK, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is content and description, but no title.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithContentAndTitle()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("content", content);
        baseObject.put("targetTitle", targetTitle);

        assertEquals(content + " " + targetTitle, sut.extractContent(BaseObjectType.BOOKMARK, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is content and description, but no title.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithDescriptionAndTitle()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("content", content);
        baseObject.put("description", description);

        assertEquals(content + " " + description, sut.extractContent(BaseObjectType.BOOKMARK, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is content only.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithContent()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("content", content);

        assertEquals(content, sut.extractContent(BaseObjectType.BOOKMARK, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is description only.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithDescription()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("description", description);

        assertEquals(description, sut.extractContent(BaseObjectType.BOOKMARK, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there is title only.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithTitle()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("targetTitle", targetTitle);

        assertEquals(targetTitle, sut.extractContent(BaseObjectType.BOOKMARK, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is Bookmark and there's no base object.
     */
    @Test
    public void testObjectToStringFromActivityBookmarkWithNoBaseObject()
    {
        assertNull(sut.extractContent(BaseObjectType.BOOKMARK, null));
    }

    /**
     * Test objectToString() when base type object is File and there is a title.
     */
    @Test
    public void testObjectToStringFromActivityFileWithTitle()
    {
        HashMap<String, String> baseObject = new HashMap<String, String>();
        baseObject.put("targetTitle", targetTitle);

        assertEquals(targetTitle, sut.extractContent(BaseObjectType.FILE, baseObject).trim());
    }

    /**
     * Test objectToString() when base type object is File and there is no title.
     */
    @Test
    public void testObjectToStringFromActivityFileWithoutTitle()
    {
        assertNull(sut.extractContent(BaseObjectType.FILE, new HashMap<String, String>()));
    }

    /**
     * Test objectToString() when base type object is File and there's no base object.
     */
    @Test
    public void testObjectToStringFromActivityFileWithNoBaseObject()
    {
        assertNull(sut.extractContent(BaseObjectType.FILE, null));
    }

}
