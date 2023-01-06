/**
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
package org.apache.apex.malhar.lib.utils.serde;

import org.junit.Assert;
import org.junit.Test;

import com.esotericsoftware.kryo.io.Input;

import com.datatorrent.netlet.util.Slice;

public class AffixSerdeTest
{
  @Test
  public void simpleTest()
  {
    SerializationBuffer buffer = new SerializationBuffer(new WindowedBlockStream());
    AffixSerde<String> serde = new AffixSerde<>(new byte[]{1, 2, 3}, new StringSerde(), new byte[]{9});

    final String orgValue = "abc";
    serde.serialize(orgValue, buffer);
    Slice slice = buffer.toSlice();

    String value = serde.deserialize(new Input(slice.buffer, slice.offset, slice.length));
    Assert.assertEquals(orgValue, value);
  }
}
