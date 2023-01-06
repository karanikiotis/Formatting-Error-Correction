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
package com.datatorrent.stram.plan.logical.requests;

import com.datatorrent.stram.plan.physical.PlanModifier;

/**
 * <p>CreateStreamRequest class.</p>
 *
 * @since 0.3.2
 */
public class CreateStreamRequest extends LogicalPlanRequest
{
  private String streamName;
  private String sourceOperatorName;
  private String sourceOperatorPortName;
  private String sinkOperatorName;
  private String sinkOperatorPortName;

  public String getStreamName()
  {
    return streamName;
  }

  public void setStreamName(String streamName)
  {
    this.streamName = streamName;
  }

  public String getSourceOperatorName()
  {
    return sourceOperatorName;
  }

  public void setSourceOperatorName(String sourceOperatorName)
  {
    this.sourceOperatorName = sourceOperatorName;
  }

  public String getSourceOperatorPortName()
  {
    return sourceOperatorPortName;
  }

  public void setSourceOperatorPortName(String sourceOperatorPortName)
  {
    this.sourceOperatorPortName = sourceOperatorPortName;
  }

  public String getSinkOperatorName()
  {
    return sinkOperatorName;
  }

  public void setSinkOperatorName(String sinkOperatorName)
  {
    this.sinkOperatorName = sinkOperatorName;
  }

  public String getSinkOperatorPortName()
  {
    return sinkOperatorPortName;
  }

  public void setSinkOperatorPortName(String sinkOperatorPortName)
  {
    this.sinkOperatorPortName = sinkOperatorPortName;
  }

  @Override
  public void execute(PlanModifier pm)
  {
    pm.addStream(streamName, sourceOperatorName, sourceOperatorPortName, sinkOperatorName, sinkOperatorPortName);
  }

}
