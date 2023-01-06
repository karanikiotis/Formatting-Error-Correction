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
package com.datatorrent.lib.testbench;

import org.junit.Assert;
import org.junit.Test;

public class RandomWordGeneratorTest
{
  @Test
  public void generateWindow()
  {
    CollectorTestSink<Object> sink = new CollectorTestSink<Object>();

    RandomWordGenerator randomWordGenerator = new RandomWordGenerator();
    randomWordGenerator.setTuplesPerWindow(10);
    randomWordGenerator.output.setSink(sink);

    randomWordGenerator.setup(null);
    randomWordGenerator.beginWindow(0);
    randomWordGenerator.emitTuples();
    randomWordGenerator.emitTuples();
    randomWordGenerator.emitTuples();
    randomWordGenerator.endWindow();

    randomWordGenerator.setup(null);
    randomWordGenerator.beginWindow(1);
    randomWordGenerator.emitTuples();
    randomWordGenerator.emitTuples();
    randomWordGenerator.emitTuples();
    randomWordGenerator.endWindow();

    Assert.assertEquals("Number of tuples", 20, sink.collectedTuples.size());
  }
}
