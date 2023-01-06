/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.streaming.storm;

import com.huawei.streaming.exception.StreamingException;
import com.huawei.streaming.exception.StreamingRuntimeException;
import com.huawei.streaming.operator.IEmitter;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * emitter实例类，每个emitter和流名称进行绑定
 *
 */
public class SpoutEmitter implements IEmitter, Serializable
{
    private static final Logger LOG = LoggerFactory.getLogger(SpoutEmitter.class);

    private static final long serialVersionUID = -4817006115667679129L;

    private boolean acker = false;

    private String streamName = null;

    private SpoutOutputCollector outputCollector;

    /**
     * <默认构造函数>
     *
     */
    public SpoutEmitter(SpoutOutputCollector collector, String name)
    {
        this(collector, name, false);
    }

    /**
     * <默认构造函数>
     *
     */
    public SpoutEmitter(SpoutOutputCollector collector, String name, boolean isAck)
    {
        if (collector == null)
        {
            LOG.error("Failed to create event emitter, storm collector is null.");
            throw new StreamingRuntimeException("Failed to create event emitter, storm collector is null.");
        }

        this.outputCollector = collector;
        this.streamName = name;
        this.acker = isAck;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emit(Object[] data)
        throws StreamingException
    {
        /**
         * 目前暂时不支持acker
         */

        if (streamName == null)
        {
            outputCollector.emit(new Values(data));
        }
        else
        {
            outputCollector.emit(streamName, new Values(data));
        }

    }

}
