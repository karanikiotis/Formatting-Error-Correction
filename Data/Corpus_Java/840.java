/**
 * Copyright (c) 2016, All Contributors (see CONTRIBUTORS file)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.eventsourcing.inmem;

import com.eventsourcing.Entity;
import com.eventsourcing.EntityHandle;
import com.eventsourcing.index.Attribute;
import com.eventsourcing.index.EqualityIndexTest;
import com.eventsourcing.inmem.MemoryIndexEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import org.testng.annotations.Test;

@Test
public class MemoryHashIndexTest extends EqualityIndexTest<HashIndex> {

    @Override public <O extends Entity> IndexedCollection<EntityHandle<O>> createIndexedCollection(Class<O> klass) {
        return new MemoryIndexEngine().createIndexedCollection(new OnHeapPersistence());
    }

    @Override
    public <A, O extends Entity> HashIndex onAttribute(Attribute<O, A> attribute) {
        return HashIndex.onAttribute(MemoryIndexEngine.compatibleAttribute(attribute));
    }
}
