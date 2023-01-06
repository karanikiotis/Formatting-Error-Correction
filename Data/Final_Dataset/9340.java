/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.map.strategy.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;
import com.gs.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;

class ImmutableMapWithHashingStrategySerializationProxy<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;
    private ImmutableMap<K, V> map;
    private HashingStrategy<? super K> hashingStrategy;

    @SuppressWarnings("UnusedDeclaration")
    public ImmutableMapWithHashingStrategySerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    ImmutableMapWithHashingStrategySerializationProxy(ImmutableMap<K, V> map, HashingStrategy<? super K> hashingStrategy)
    {
        this.map = map;
        this.hashingStrategy = hashingStrategy;
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeObject(this.hashingStrategy);
        out.writeInt(this.map.size());
        try
        {
            this.map.forEachKeyValue(new CheckedProcedure2<K, V>()
            {
                public void safeValue(K key, V value) throws IOException
                {
                    out.writeObject(key);
                    out.writeObject(value);
                }
            });
        }
        catch (RuntimeException e)
        {
            if (e.getCause() instanceof IOException)
            {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        HashingStrategy<? super K> strategy = (HashingStrategy<? super K>) in.readObject();
        int size = in.readInt();
        MutableMap<K, V> deserializedMap = UnifiedMapWithHashingStrategy.newMap(strategy);

        for (int i = 0; i < size; i++)
        {
            deserializedMap.put((K) in.readObject(), (V) in.readObject());
        }

        this.map = deserializedMap.toImmutable();
    }

    protected Object readResolve()
    {
        return this.map;
    }
}
