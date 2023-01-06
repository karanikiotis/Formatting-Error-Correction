/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.gs.fw.common.mithra.test.cacheloader;

import com.gs.fw.common.mithra.extractor.Extractor;
import com.gs.fw.common.mithra.extractor.IntExtractor;

import java.sql.Timestamp;


class MockExtractor implements IntExtractor
{
    public int intValueOf(Object o)
    {
        return ((MockObject) o).nonUniqueKey;
    }

    public void setIntValue(Object o, int newValue)
    {
        ((MockObject) o).nonUniqueKey = newValue;
    }

    public void setValue(Object o, Object newValue)
    {
    }

    public void setValueNull(Object o)
    {
    }

    public void setValueUntil(Object o, Object newValue, Timestamp exclusiveUntil)
    {
    }

    public void setValueNullUntil(Object o, Timestamp exclusiveUntil)
    {
    }

    public boolean isAttributeNull(Object o)
    {
        return false;
    }

    public boolean valueEquals(Object first, Object second, Extractor secondExtractor)
    {
        return ((MockObject) first).nonUniqueKey == ((MockObject) second).nonUniqueKey;
    }

    public int valueHashCode(Object object)
    {
        return ((MockObject) object).nonUniqueKey;
    }

    public boolean valueEquals(Object first, Object second)
    {
        return ((MockObject) first).nonUniqueKey == ((MockObject) second).nonUniqueKey;
    }

    public Object valueOf(Object object)
    {
        return ((MockObject) object).nonUniqueKey;
    }
}
