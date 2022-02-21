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
package com.datatorrent.benchmark.stream;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context.PortContext;
import com.datatorrent.api.DAG;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.lib.stream.DevNull;
import com.datatorrent.lib.stream.StreamDuplicater;

/**
 * Benchmark App for StreamDuplicater Operator.
 * This operator is benchmarked to emit 1,700,000 tuples/sec on cluster node.
 *
 * @since 2.0.0
 */
@ApplicationAnnotation(name = "StreamDuplicaterApp")
public class StreamDuplicaterApp implements StreamingApplication
{
  private final Locality locality = null;
  public static final int QUEUE_CAPACITY = 16 * 1024;

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    // RandomEventGenerator rand = dag.addOperator("rand", new RandomEventGenerator());
    // rand.setMinvalue(0);
    // rand.setMaxvalue(999999);
    // rand.setTuplesBlastIntervalMillis(50);
    // dag.getMeta(rand).getMeta(rand.integer_data).getAttributes().put(PortContext.QUEUE_CAPACITY, QUEUE_CAPACITY);
    IntegerOperator intInput = dag.addOperator("intInput", new IntegerOperator());
    StreamDuplicater stream = dag.addOperator("stream", new StreamDuplicater());
    dag.getMeta(stream).getMeta(stream.data).getAttributes().put(PortContext.QUEUE_CAPACITY, QUEUE_CAPACITY);
    dag.addStream("streamdup1", intInput.integer_data, stream.data).setLocality(locality);
    DevNull<Integer> dev1 = dag.addOperator("dev1", new DevNull());
    DevNull<Integer> dev2 = dag.addOperator("dev2", new DevNull());
    dag.addStream("streamdup2", stream.out1, dev1.data).setLocality(locality);
    dag.addStream("streamdup3", stream.out2, dev2.data).setLocality(locality);

  }

}
