/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.jmh.map;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import org.apache.commons.lang.RandomStringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import scala.collection.mutable.AnyRefMap;
import scala.collection.mutable.Map;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ScalaAnyRefMapGetTest extends AbstractJMHTestRunner
{
    private static final int RANDOM_COUNT = 9;

    @Param({"250000", "500000", "750000", "1000000", "1250000", "1500000", "1750000", "2000000", "2250000", "2500000", "2750000", "3000000",
            "3250000", "3500000", "3750000", "4000000", "4250000", "4500000", "4750000", "5000000", "5250000", "5500000", "5750000", "6000000",
            "6250000", "6500000", "6750000", "7000000", "7250000", "7500000", "7750000", "8000000", "8250000", "8500000", "8750000", "9000000",
            "9250000", "9500000", "9750000", "10000000"})
    public int size;
    private String[] elements;
    private Map<String, String> scalaAnyRefMap;

    @Setup
    public void setUp()
    {
        Random random = new Random(123456789012345L);

        this.elements = new String[this.size];
        this.scalaAnyRefMap = new AnyRefMap<>(this.size);

        for (int i = 0; i < this.size; i++)
        {
            String element = RandomStringUtils.random(RANDOM_COUNT, 0, 0, false, true, null, random);
            this.elements[i] = element;
            this.scalaAnyRefMap.put(element, "dummy");
        }
    }

    @Benchmark
    public void get()
    {
        int localSize = this.size;
        String[] localElements = this.elements;
        Map<String, String> localScalaAnyRefMap = this.scalaAnyRefMap;

        for (int i = 0; i < localSize; i++)
        {
            if (!localScalaAnyRefMap.get(localElements[i]).isDefined())
            {
                throw new AssertionError(i);
            }
        }
    }
}
