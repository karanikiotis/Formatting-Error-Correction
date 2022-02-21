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

import com.gs.fw.common.mithra.attribute.Attribute;
import com.gs.fw.common.mithra.cache.FullUniqueIndex;
import com.gs.fw.common.mithra.cacheloader.*;
import com.gs.fw.common.mithra.extractor.Extractor;
import com.gs.fw.common.mithra.test.domain.StockFinder;
import com.gs.fw.common.mithra.util.MithraRuntimeCacheController;

import java.util.List;


class MockTaskSpawner extends DependentLoadingTaskSpawner
{
    public MockTaskSpawner(CacheLoaderEngine engine)
    {
        super(new Attribute[1], "", null, null, new MithraRuntimeCacheController(StockFinder.class), new CacheLoaderContext(new CacheLoaderManagerImpl(), null), null, null);
    }

    protected LoadingTaskRunner createTaskRunner(List keyHolders, Extractor[] keyExtractors, LoadingTaskRunner.State state)
    {
        return null;
    }

    protected List addOwnersToLoadIfAbsent(FullUniqueIndex index, List stripe, Extractor[] ownerKeyExtractor)
    {
        return stripe;
    }
}
