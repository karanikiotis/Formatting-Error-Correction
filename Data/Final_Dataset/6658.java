/*
 * Copyright (C) 2012 Sony Mobile Communications AB
 *
 * This file is part of ApkAnalyser.
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

package mereflect.info;

public abstract class Abstract8ByteTypeInfo extends AbstractClassInfo
{
    protected int ID_BYTES_HI = 0;

    protected int ID_BYTES_LO = 1;

    public Abstract8ByteTypeInfo(long hi, long lo)
    {
        setValue(ID_BYTES_HI, new Long(hi));
        setValue(ID_BYTES_LO, new Long(lo));
    }

    public long getBytesHi()
    {
        return ((Long) getValue(ID_BYTES_HI)).longValue();
    }

    public long getBytesLo()
    {
        return ((Long) getValue(ID_BYTES_LO)).longValue();
    }

    @Override
    public int getLength()
    {
        return 9;
    }
}
