/*
 * Copyright (C) 2015 Sebastian Daschner, sebastian-daschner.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sebastian_daschner.jaxrs_analyzer.analysis.classes.testclasses.resource.response;

import com.sebastian_daschner.jaxrs_analyzer.model.Types;
import com.sebastian_daschner.jaxrs_analyzer.model.elements.HttpResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.singletonList;

public class TestClass23 {

    // continue testing all available non-static Response methods
    @javax.ws.rs.GET
    public Response method() {
        Response.ResponseBuilder responseBuilder = Response.ok();
        responseBuilder.entity(12L, new Annotation[0]);
        responseBuilder.link("", "rel");
        responseBuilder.status(Response.Status.EXPECTATION_FAILED);
        responseBuilder.tag("");
        responseBuilder.type(MediaType.APPLICATION_XML);
        responseBuilder.variants();

        return responseBuilder.build();
    }

    public static Set<HttpResponse> getResult() {
        final HttpResponse result = new HttpResponse();

        result.getStatuses().addAll(Arrays.asList(200, 417));
        result.getEntityTypes().addAll(singletonList(Types.LONG));
        result.getHeaders().addAll(Arrays.asList("Link", "ETag", "Vary"));
        result.getContentTypes().add("application/xml");

        return Collections.singleton(result);
    }

}
