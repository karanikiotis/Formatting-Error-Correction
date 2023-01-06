/*
 *      Copyright (C) 2012, 2016  higherfrequencytrading.com
 *      Copyright (C) 2016 Roman Leventov
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.openhft.chronicle.map;

import org.junit.Ignore;
import org.junit.Test;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * Created by peter.lawrey on 06/12/14.
 */
public class LargeEntriesTest {

    @Test
    public void testLargeStrings() throws ExecutionException, InterruptedException, IOException {
        final int ENTRIES = 250;
        final int ENTRY_SIZE = 100 * 1024;

        File file = File.createTempFile("largeEntries" + System.currentTimeMillis(), ".deleteme");
        file.deleteOnExit();
        try (final ChronicleMap<String, String> map = ChronicleMapBuilder
                .of(String.class, String.class)
//                .valueReaderAndDataAccess(, SnappyStringMarshaller.INSTANCE, )
                .actualSegments(1) // to force an error.
                .entries(ENTRIES)
                .averageKeySize(10)
                .averageValueSize(ENTRY_SIZE)
                .putReturnsNull(true)
                .createPersistedTo(file)) {
            warmUpCompression(ENTRY_SIZE);
            int threads = 4; //Runtime.getRuntime().availableProcessors();
            ExecutorService es = Executors.newFixedThreadPool(threads);
            final int block = ENTRIES / threads;
            for (int i = 0; i < 3; i++) {
                long start = System.currentTimeMillis();
                List<Future<?>> futureList = new ArrayList<>();
                for (int t = 0; t < threads; t++) {
                    final int finalT = t;
                    futureList.add(es.submit(new Runnable() {
                        @Override
                        public void run() {
                            exerciseLargeStrings(map, finalT * block, finalT * block + block,
                                    ENTRY_SIZE);
                        }
                    }));
                }
                for (Future<?> future : futureList) {
                    future.get();
                }
                long time = System.currentTimeMillis() - start;
                long operations = 3;
                System.out.printf("Put/Get rate was %.1f MB/s%n",
                        operations * ENTRIES * ENTRY_SIZE / 1e6 / (time / 1e3));
            }
            es.shutdown();
            es.awaitTermination(1, TimeUnit.MINUTES);
            assertTrue(es.isTerminated());
        }
        file.delete();
    }

    private void warmUpCompression(int entrySize) {
//        String value = generateValue(entrySize);
//        DirectBytes bytes = DirectStore.allocate(entrySize / 6).bytes();
//        for (int i = 0; i < 5; i++) {
//            // warmup to compression.
//            bytes.clear();
//            SnappyStringMarshaller.INSTANCE.write(bytes, value);
//            bytes.flip();
//            SnappyStringMarshaller.INSTANCE.read(bytes);
//        }
//        bytes.release();
    }

    @Test
    @Ignore("Performance Test")
    public void testLargeStringsPerf() throws ExecutionException, InterruptedException, IOException {
        doLargeEntryPerf(10000, 100 * 1024);
        doLargeEntryPerf(1000000, 1024);
        doLargeEntryPerf(100000, 10 * 1024);
        doLargeEntryPerf(10000, 100 * 1024);
        doLargeEntryPerf(3000, 1024 * 1024);
    }

    private void doLargeEntryPerf(int ENTRIES, final int ENTRY_SIZE) throws IOException, InterruptedException, ExecutionException {
        System.out.printf("Testing %,d entries of %,d KB%n", ENTRIES, ENTRY_SIZE / 1024);
        File file = File.createTempFile("largeEntries", ".deleteme");
        file.deleteOnExit();
        final ChronicleMap<String, String> map = ChronicleMapBuilder
                .of(String.class, String.class)
//                .valueReaderAndDataAccess(, SnappyStringMarshaller.INSTANCE, )
                .entries(ENTRIES)
                .averageKeySize(10)
                .averageValueSize(ENTRY_SIZE)
                .putReturnsNull(true)
                .createPersistedTo(file);
        {
//            warmUpCompression(ENTRY_SIZE);
            int threads = Runtime.getRuntime().availableProcessors();
            ExecutorService es = Executors.newFixedThreadPool(threads);
            final int block = ENTRIES / threads;
            for (int i = 0; i < 3; i++) {
                long start = System.currentTimeMillis();
                List<Future<?>> futureList = new ArrayList<>();
                for (int t = 0; t < threads; t++) {
                    final int finalT = t;
                    futureList.add(es.submit(new Runnable() {
                        @Override
                        public void run() {
                            exerciseLargeStrings(map, finalT * block, finalT * block + block,
                                    ENTRY_SIZE);
                        }
                    }));
                }
                for (Future<?> future : futureList) {
                    future.get();
                }
                long time = System.currentTimeMillis() - start;
                long operations = 3;
                System.out.printf("Put/Get rate was %.1f MB/s%n", operations * ENTRIES * ENTRY_SIZE / 1e6 / (time / 1e3));
            }
            es.shutdown();
            if (es.isTerminated())
                map.close();
        }
    }

    void exerciseLargeStrings(ChronicleMap<String, String> map, int start, int finish, int entrySize) {
/*
        final Thread thisThread = Thread.currentThread();
        Thread monitor = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append(thisThread).append("\n");
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Jvm.pause(50);
                        StackTraceElement[] stackTrace = thisThread.getStackTrace();
                        for (int i = 0; i < 4 && i < stackTrace.length; i++)
                            sb.append("\tat ").append(stackTrace[i]).append("\n");
                        sb.append("\n");
                    }
                } catch (InterruptedException e) {
                }
                System.out.println(sb);
            }
        });
        monitor.start();
*/
        String value = generateValue(entrySize);

        for (int i = start; i < finish; i++) {
            String key = "key-" + i;
            String object;

            map.put(key, value);
            object = map.get(key);
            assertTrue(key, map.containsKey(key));

            assertNotNull(key, object);
            assertEquals(key, entrySize, object.length());
        }
//        monitor.interrupt();

        for (int i = start; i < finish; i++) {
//            System.out.println(i);
            String key = "key-" + i;

            String object = map.get(key);

            assertNotNull(key, object);
            assertEquals(key, entrySize, object.length());
        }
    }

    private String generateValue(int entrySize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder xml = new XMLEncoder(baos);
        Map<String, String> map2 = new HashMap<>();
        Random rand = new Random(1);
        for (int i = 0; i < entrySize / 80; i++)
            map2.put("key-" + i, "value-" + rand.nextInt(1000));
        xml.writeObject(map2);
        xml.close();
        return baos.toString().substring(0, entrySize);
    }
}
