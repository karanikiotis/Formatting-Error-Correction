/*
 * Copyright (C) Scott Cranton, Jakub Korab, and Christian Posta
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.extend.dataformat;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EbcdicDataFormatSpringTest extends CamelSpringTestSupport {
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/dataformat-ebcdic-context.xml");
    }

    @Test
    public void testMarshalEbcdicSpring() throws Exception {
        final String ascii = "Mainframes rock!";
        final byte[] expected = {-44, -127, -119, -107, -122, -103, -127, -108, -123, -94, 64, -103, -106, -125, -110, 90};

        final byte[] result = template.requestBody("direct:marshal", ascii, byte[].class);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testUnmarshalEbcdicSpring() throws Exception {
        final byte[] ebcdic = {-29, -119, -108, -123, 64, -93, -106, 64, -92, -105, -121, -103, -127, -124, -123, 75, 75, 75};

        final String result = template.requestBody("direct:unmarshal", ebcdic, String.class);

        assertEquals("Time to upgrade...", result);
    }
}
