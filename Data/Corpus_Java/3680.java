/*
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
package com.facebook.presto.benchmark;

import com.facebook.presto.Session;
import com.facebook.presto.execution.TaskId;
import com.facebook.presto.execution.TaskStateMachine;
import com.facebook.presto.memory.MemoryPool;
import com.facebook.presto.memory.QueryContext;
import com.facebook.presto.operator.Driver;
import com.facebook.presto.operator.TaskContext;
import com.facebook.presto.plugin.memory.MemoryConnectorFactory;
import com.facebook.presto.spi.Page;
import com.facebook.presto.spi.QueryId;
import com.facebook.presto.spi.memory.MemoryPoolId;
import com.facebook.presto.spiller.SpillSpaceTracker;
import com.facebook.presto.testing.LocalQueryRunner;
import com.facebook.presto.testing.PageConsumerOperator;
import com.facebook.presto.tpch.TpchConnectorFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.airlift.units.DataSize;
import org.intellij.lang.annotations.Language;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static com.facebook.presto.testing.TestingSession.testSessionBuilder;
import static io.airlift.units.DataSize.Unit.GIGABYTE;

public class MemoryLocalQueryRunner
{
    protected final LocalQueryRunner localQueryRunner;
    protected final Session session;

    public MemoryLocalQueryRunner()
    {
        this(ImmutableMap.of());
    }

    public MemoryLocalQueryRunner(Map<String, String> properties)
    {
        Session.SessionBuilder sessionBuilder = testSessionBuilder()
                .setCatalog("memory")
                .setSchema("default");
        properties.forEach(sessionBuilder::setSystemProperty);

        session = sessionBuilder.build();
        localQueryRunner = createMemoryLocalQueryRunner(session);
    }

    public List<Page> execute(@Language("SQL") String query)
    {
        ExecutorService executor = localQueryRunner.getExecutor();
        MemoryPool memoryPool = new MemoryPool(new MemoryPoolId("test"), new DataSize(2, GIGABYTE));
        MemoryPool systemMemoryPool = new MemoryPool(new MemoryPoolId("testSystem"), new DataSize(2, GIGABYTE));

        SpillSpaceTracker spillSpaceTracker = new SpillSpaceTracker(new DataSize(1, GIGABYTE));
        TaskContext taskContext = new QueryContext(new QueryId("test"), new DataSize(1, GIGABYTE), memoryPool, systemMemoryPool, executor, new DataSize(4, GIGABYTE), spillSpaceTracker)
                .addTaskContext(new TaskStateMachine(new TaskId("query", 0, 0), executor),
                        session,
                        false,
                        false);

        // Use NullOutputFactory to avoid coping out results to avoid affecting benchmark results
        ImmutableList.Builder<Page> output = ImmutableList.builder();
        List<Driver> drivers = localQueryRunner.createDrivers(
                query,
                new PageConsumerOperator.PageConsumerOutputFactory(types -> output::add),
                taskContext);

        boolean done = false;
        while (!done) {
            boolean processed = false;
            for (Driver driver : drivers) {
                if (!driver.isFinished()) {
                    driver.process();
                    processed = true;
                }
            }
            done = !processed;
        }

        return output.build();
    }

    private static LocalQueryRunner createMemoryLocalQueryRunner(Session session)
    {
        LocalQueryRunner localQueryRunner = LocalQueryRunner.queryRunnerWithInitialTransaction(session);

        // add tpch
        localQueryRunner.createCatalog("tpch", new TpchConnectorFactory(1), ImmutableMap.of());
        localQueryRunner.createCatalog(
                "memory",
                new MemoryConnectorFactory(),
                ImmutableMap.of("memory.max-data-per-node", "4GB"));

        return localQueryRunner;
    }
}
