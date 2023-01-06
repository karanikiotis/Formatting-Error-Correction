/*
 * Copyright 2009-2016 DigitalGlobe, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package org.mrgeo.aggregators;

import org.hamcrest.core.AnyOf;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mrgeo.junit.UnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@SuppressWarnings("all") // test code, not included in production
public class ModeAggregatorTest
{
private static double epsilon = 0.0000001;

@Test
@Category(UnitTest.class)
public void testDouble()
{
  double[] values = {0.21, 0.32, 0.32, 0.54};
  double nodata = Double.NaN;
  double result;
  Aggregator agg = new ModeAggregator();

  //Test normal case
  result = agg.aggregate(values, nodata);
  assertEquals(0.32, result, epsilon);

  //Test nodata cases
  values[0] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(0.32, result, epsilon);

  values[1] = nodata;
  result = agg.aggregate(values, nodata);
  assertThat((double) result, AnyOf.anyOf(IsCloseTo.closeTo(0.32f, epsilon), IsCloseTo.closeTo(0.54f, epsilon)));

  values[2] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(0.54, result, epsilon);

  values[3] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(nodata, result, epsilon);

}

@Test
@Category(UnitTest.class)
public void testFloat()
{
  float[] values = {0.21f, 0.32f, 0.32f, 0.54f};
  float nodata = -9999.0f;
  float result;
  Aggregator agg = new ModeAggregator();

  //Test normal case
  result = agg.aggregate(values, nodata);
  assertEquals(0.32, result, epsilon);

  //Test nodata cases
  values[0] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(0.32, result, epsilon);

  values[1] = nodata;
  result = agg.aggregate(values, nodata);
  //assertEquals(0.54, result, epsilon);
  assertThat((double) result, AnyOf.anyOf(IsCloseTo.closeTo(0.32f, epsilon), IsCloseTo.closeTo(0.54f, epsilon)));

  values[2] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(0.54, result, epsilon);

  values[3] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(nodata, result, epsilon);

}

@Test
@Category(UnitTest.class)
public void testInt()
{
  int[] values = {1, 2, 2, 4};
  int nodata = -9999;
  int result;
  Aggregator agg = new ModeAggregator();

  //Test normal case
  result = agg.aggregate(values, nodata);
  assertEquals(2, result);

  //Test nodata cases
  values[0] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(2, result);

  values[1] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(2, result);

  values[2] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(4, result);

  values[3] = nodata;
  result = agg.aggregate(values, nodata);
  assertEquals(nodata, result);

}

}
