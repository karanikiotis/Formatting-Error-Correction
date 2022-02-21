/*
 * Copyright 2017 Huawei Technologies Co., Ltd
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

package io.servicecomb.transport.highway;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.netty.buffer.ByteBuf;
import io.protostuff.Input;
import io.protostuff.runtime.ProtobufCompatibleUtils;
import io.protostuff.runtime.ProtobufFeature;
import io.servicecomb.codec.protobuf.definition.OperationProtobuf;
import io.servicecomb.codec.protobuf.utils.WrapSchema;
import io.servicecomb.codec.protobuf.utils.schema.NotWrapSchema;
import io.servicecomb.core.Invocation;
import io.servicecomb.core.definition.OperationMeta;
import io.servicecomb.core.definition.SchemaMeta;
import io.servicecomb.foundation.vertx.client.tcp.TcpData;
import io.servicecomb.foundation.vertx.server.TcpParser;
import io.servicecomb.foundation.vertx.tcp.TcpOutputStream;
import io.servicecomb.serviceregistry.RegistryUtils;
import io.servicecomb.serviceregistry.ServiceRegistry;
import io.servicecomb.serviceregistry.registry.ServiceRegistryFactory;
import io.servicecomb.swagger.invocation.Response;
import io.servicecomb.transport.highway.message.RequestHeader;
import io.servicecomb.transport.highway.message.ResponseHeader;
import io.vertx.core.buffer.Buffer;
import mockit.Mock;
import mockit.MockUp;

public class TestHighwayCodec {

    private RequestHeader header = null;

    private OperationProtobuf operationProtobuf = null;

    private Buffer bodyBuffer = null;

    private WrapSchema schema = null;

    private SchemaMeta schemaMeta = null;

    private OperationMeta operationMeta = null;

    private ByteBuf lByteBuf = null;

    private ByteBuffer nioBuffer = null;

    private Invocation invocation = null;

    @BeforeClass
    public static void setupClass() {
        ProtobufCompatibleUtils.init();
        HighwayCodec.setHighwayTransport(new HighwayTransport());
    }

    @Before
    public void setUp() throws Exception {
        ServiceRegistry serviceRegistry = ServiceRegistryFactory.createLocal();
        serviceRegistry.getMicroserviceManager().addMicroservice("app", "ms");
        serviceRegistry.init();
        RegistryUtils.setServiceRegistry(serviceRegistry);

        header = Mockito.mock(RequestHeader.class);

        operationProtobuf = Mockito.mock(OperationProtobuf.class);

        bodyBuffer = Mockito.mock(Buffer.class);

        schema = Mockito.mock(WrapSchema.class);

        schemaMeta = Mockito.mock(SchemaMeta.class);

        operationMeta = Mockito.mock(OperationMeta.class);

        lByteBuf = Mockito.mock(ByteBuf.class);

        nioBuffer = Mockito.mock(ByteBuffer.class);

        invocation = Mockito.mock(Invocation.class);
    }

    @After
    public void tearDown() throws Exception {

        header = null;

        operationProtobuf = null;

        bodyBuffer = null;

        schema = null;

        schemaMeta = null;

        operationMeta = null;

        lByteBuf = null;

        nioBuffer = null;

        invocation = null;
    }

    @Test
    public void testDecodeRequest() {
        boolean status = true;

        try {
            commonMock();
            Invocation inv = HighwayCodec.decodeRequest(header, operationProtobuf, bodyBuffer, null);
            Assert.assertNotNull(inv);
        } catch (Exception e) {
            status = false;
        }
        Assert.assertTrue(status);
    }

    @Test
    public void testDecodeResponse() throws Exception {
        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(operationProtobuf.findResponseSchema(200)).thenReturn(Mockito.mock(WrapSchema.class));

        Map<String, String> context = new HashMap<>();
        Mockito.when(invocation.getContext()).thenReturn(context);

        TcpData tcpData = Mockito.mock(TcpData.class);

        Mockito.when(tcpData.getHeaderBuffer()).thenReturn(bodyBuffer);
        commonMock();

        ResponseHeader header = new ResponseHeader();
        header.setStatusCode(200);
        header.setContext(new HashMap<String, String>());
        header.getContext().put("a", "10");
        Buffer responseBuf = HighwayCodec.encodeResponse(0, header, null, null, new ProtobufFeature());

        TcpData tcp = new TcpData(responseBuf.slice(23, responseBuf.length()), null);
        Response response = HighwayCodec.decodeResponse(invocation, operationProtobuf, tcp, null);
        Assert.assertEquals("10", invocation.getContext().get("a"));
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testEncodeResponse() {
        boolean status = true;
        WrapSchema bodySchema = Mockito.mock(WrapSchema.class);
        try {
            commonMock();
            HighwayCodec.encodeResponse(23432142, null, bodySchema, new Object(), new ProtobufFeature());
        } catch (Exception e) {
            status = false;
        }
        Assert.assertTrue(status);
    }

    @Test
    public void testEncodeRequest() {
        boolean status = true;
        try {
            commonMock();
            TcpOutputStream os = HighwayCodec.encodeRequest(0, invocation, operationProtobuf, null);
            Assert.assertNotNull(os);
            Assert.assertArrayEquals(TcpParser.TCP_MAGIC, os.getBuffer().getBytes(0, 7));
        } catch (Exception e) {
            status = false;
        }
        Assert.assertTrue(status);
    }

    @Test
    public void testReadRequestHeader() {
        boolean status = true;
        try {
            new MockUp<NotWrapSchema>() {
                @Mock
                public Object readObject(Input input) throws IOException {
                    return new RequestHeader();
                }

            };
            bodyBuffer = Buffer.buffer("\"abc\"");
            RequestHeader requestHeader = HighwayCodec.readRequestHeader(bodyBuffer, null);
            Assert.assertNotNull(requestHeader);
            Assert.assertEquals(0, requestHeader.getFlags());
        } catch (Exception e) {
            status = false;
        }
        Assert.assertTrue(status);
    }

    private void commonMock() {
        Mockito.when(operationProtobuf.getRequestSchema()).thenReturn(schema);
        Mockito.when(bodyBuffer.getByteBuf()).thenReturn(lByteBuf);
        Mockito.when(lByteBuf.nioBuffer()).thenReturn(nioBuffer);
        Mockito.when(operationProtobuf.getOperationMeta()).thenReturn(operationMeta);
        Mockito.when(operationMeta.getSchemaMeta()).thenReturn(schemaMeta);
    }

}
