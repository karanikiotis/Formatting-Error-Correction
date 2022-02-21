/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.common;

import org.elasticsearch.test.ESTestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class ParseFieldTests extends ESTestCase {

    @Test
    public void testParse() {
        String name = "foo_bar";
        String camelCase = "fooBar";
        ParseField field = new ParseField(name);
        String[] deprecated = new String[]{"barFoo", "bar_foo"};
        ParseField withDeprecations = field.withDeprecation("Foobar", randomFrom(deprecated));
        assertThat(field, not(sameInstance(withDeprecations)));
        assertThat(field.match(name, false), is(true));
        assertThat(field.match(camelCase, false), is(true));
        assertThat(field.match("foo bar", false), is(false));
        assertThat(field.match(randomFrom(deprecated), false), is(false));
        assertThat(field.match("barFoo", false), is(false));

        assertThat(withDeprecations.match(name, false), is(true));
        assertThat(withDeprecations.match(camelCase, false), is(true));
        assertThat(withDeprecations.match("foo bar", false), is(false));
        assertThat(withDeprecations.match(randomFrom(deprecated), false), is(true));
        assertThat(withDeprecations.match("barFoo", false), is(true));

        // now with strict mode
        assertThat(field.match(name, true), is(true));
        assertThat(field.match("foo bar", true), is(false));
        assertThat(field.match(randomFrom(deprecated), true), is(false));
        assertThat(field.match("barFoo", true), is(false));

        assertThat(withDeprecations.match(name, true), is(true));
        assertThat(withDeprecations.match("foo bar", true), is(false));
        try {
            withDeprecations.match(randomFrom(deprecated), true);
            fail();
        } catch (IllegalArgumentException ex) {

        }

        try {
            withDeprecations.match("barFoo", true);
            fail();
        } catch (IllegalArgumentException ex) {

        }

        try {
            withDeprecations.match(camelCase, true);
            fail();
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAllDeprecated() {
        String[] values = new String[]{"like_text", "likeText"};

        boolean withDeprecatedNames = randomBoolean();
        String[] deprecated = new String[]{"text", "same_as_text"};
        String[] allValues = values;
        if (withDeprecatedNames) {
            String[] newArray = new String[allValues.length + deprecated.length];
            System.arraycopy(allValues, 0, newArray, 0, allValues.length);
            System.arraycopy(deprecated, 0, newArray, allValues.length, deprecated.length);
            allValues = newArray;
        }

        ParseField field = new ParseField(randomFrom(values));
        if (withDeprecatedNames) {
            field = field.withDeprecation(deprecated);
        }
        field = field.withAllDeprecated("like");

        // strict mode off
        assertThat(field.match(randomFrom(allValues), false), is(true));
        assertThat(field.match("not a field name", false), is(false));

        // now with strict mode
        try {
            field.match(randomFrom(allValues), true);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
}
