/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hyracks.http.servlet;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentMap;

import org.apache.hyracks.http.api.IServletRequest;
import org.apache.hyracks.http.api.IServletResponse;
import org.apache.hyracks.http.server.AbstractServlet;
import org.apache.hyracks.http.server.utils.HttpUtil;

import io.netty.handler.codec.http.HttpResponseStatus;

public class SlowServlet extends AbstractServlet {
    public SlowServlet(ConcurrentMap<String, Object> ctx, String[] paths) {
        super(ctx, paths);
    }

    @Override
    protected void get(IServletRequest request, IServletResponse response) throws Exception {
        response.setStatus(HttpResponseStatus.OK);
        Thread.sleep(5000); // NOSONAR
        HttpUtil.setContentType(response, HttpUtil.ContentType.TEXT_HTML, HttpUtil.Encoding.UTF8);
        response.outputStream().write("I am playing hard to get".getBytes(StandardCharsets.UTF_8));
    }
}
