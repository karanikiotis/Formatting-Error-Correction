/*
 * Copyright 2014 Higher Frequency Trading
 * <p/>
 * http://www.higherfrequencytrading.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.collections;

import net.openhft.collections.jrs166.JSR166TestCase;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Rob Austin.
 */
public class TimeBasedReplicationTests extends JSR166TestCase {


    public static final byte IDENTIFIER = 1;

    private static File getPersistenceFile() {
        String TMP = System.getProperty("java.io.tmpdir");
        File file = new File(TMP + "/shm-test" + System.nanoTime());
        file.deleteOnExit();
        return file;
    }


    @Test
    public void testIgnoreALatePut() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);

        SharedHashMap map = new SharedHashMapBuilder().entries(10)
                .canReplicate(true)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(map.size(), 1);
        assertEquals(map.get("key-1"), "value-1");

        // now test assume that we receive a late update to the map, the following update should be ignored
        late(timeProvider);


        map.put("key-1", "value-2");

        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(map.size(), 1);
        assertEquals(map.get("key-1"), "value-1");

    }

    @Test
    public void testIgnoreALatePutIfAbsent() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        SharedHashMap map = new SharedHashMapBuilder().entries(10)
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(map.size(), 1);
        assertEquals(map.get("key-1"), "value-1");

        // now test assume that we receive a late update to the map, the following update should be ignored
        late(timeProvider);


        final Object o = map.putIfAbsent("key-1", "value-2");
        assertEquals(o, null);

        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(1, map.size());
        assertEquals(map.get("key-1"), "value-1");

    }

    @Test
    public void testIgnoreALateReplace() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        SharedHashMap map = new SharedHashMapBuilder().entries(10)
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);


        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));


        // now test assume that we receive a late update to the map, the following update should be ignored
        late(timeProvider);


        final Object o = map.replace("key-1", "value-2");
        assertEquals(o, null);

        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(map.size(), 1);
        assertEquals("value-1", map.get("key-1"));

    }

    @Test
    public void testIgnoreALateReplaceWithValue() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        SharedHashMap map = new SharedHashMapBuilder().entries(10)
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

        // now test assume that we receive a late update to the map, the following update should be ignored
        late(timeProvider);


        assertEquals(null, map.replace("key-1", "value-2"));


        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

    }

    @Test
    public void testIgnoreALateRemoveWithValue() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        SharedHashMap map = new SharedHashMapBuilder()
                .canReplicate(true)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

        // now test assume that we receive a late update to the map, the following update should be ignored
        late(timeProvider);


        assertEquals(false, map.remove("key-1", "value-1"));

        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

    }

    @Test
    public void testIgnoreALateRemove() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        SharedHashMap map = new SharedHashMapBuilder()
                .canReplicate(true)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

        // now test assume that we receive a late update to the map, the following update should be ignored
        late(timeProvider);

        map.remove("key-1");

        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

    }


    @Test
    public void testIgnoreWithRemoteRemove() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        VanillaSharedReplicatedHashMap map = (VanillaSharedReplicatedHashMap) new SharedHashMapBuilder()
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

        // now test assume that we receive a late update to the map, the following update should be ignored
        final long late = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(5);
        assertEquals(null, map.remove("key-1", "value-2", IDENTIFIER, late));

        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));
        assertTrue(map.containsValue("value-1"));
        assertFalse(map.containsValue("value-2"));
    }


    @Test
    public void testIgnoreWithRemotePut() throws IOException {


        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        VanillaSharedReplicatedHashMap map = (VanillaSharedReplicatedHashMap) new SharedHashMapBuilder()
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        assertEquals(1, map.size());
        assertEquals("value-1", map.get("key-1"));

        // now test assume that we receive a late update to the map, the following update should be ignored
        // now test assume that we receive a late update to the map, the following update should be ignored
        final long late = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(5);
        assertEquals(null, map.put("key-1", "value-2", IDENTIFIER, late));


        // we'll now flip the time back to the current in order to do the read the result
        current(timeProvider);

        assertEquals("value-1", map.get("key-1"));
        assertEquals(1, map.size(), 0);
    }


    @Test
    public void testRemoveFollowedByLatePut() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        VanillaSharedReplicatedHashMap map = (VanillaSharedReplicatedHashMap) new SharedHashMapBuilder().entries(10)
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        map.remove("key-1", "value-1");
        assertEquals(0, map.size());
        assertEquals(null, map.get("key-1"));
        assertEquals(false, map.containsKey("key-1"));

        // test assume that we receive a late update to the map, the following update should be ignored
        final long late = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(50);
        assertEquals(null, map.put("key-1", "value-2", IDENTIFIER, late));

        assertEquals(null, map.get("key-1"));
        assertEquals(false, map.containsKey("key-1"));
        assertEquals(0, map.size(), 0);
    }


    @Test
    public void testPutRemovePut() throws IOException {

        final TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        SharedHashMap map = new SharedHashMapBuilder().entries(10)
                .canReplicate(true)
                .entries(10)
                .identifier((byte) 1)
                .timeProvider(timeProvider).file(getPersistenceFile()).kClass(CharSequence.class).vClass(CharSequence.class).create();

        current(timeProvider);

        // we do a put at the current time
        map.put("key-1", "value-1");
        map.remove("key-1");
        assertEquals(0, map.size());
        assertEquals(null, map.put("key-1", "new-value-2"));
        assertEquals(true, map.containsKey("key-1"));
        assertEquals("new-value-2", map.get("key-1"));
        assertEquals(1, map.size(), 0);
    }

    private void current(TimeProvider timeProvider) {
        Mockito.when(timeProvider.currentTimeMillis()).thenReturn(System.currentTimeMillis());
    }

    private void late(TimeProvider timeProvider) {
        Mockito.when(timeProvider.currentTimeMillis()).thenReturn(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(5));
    }
}
