package com.insightfullogic.honest_profiler.core.aggregation.aggregator;

import static com.insightfullogic.honest_profiler.framework.ParameterUtil.getScenariosAndGroupings;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.insightfullogic.honest_profiler.core.aggregation.grouping.FrameGrouping;
import com.insightfullogic.honest_profiler.core.aggregation.grouping.ThreadGrouping;
import com.insightfullogic.honest_profiler.framework.checker.FlatCheckAdapter;
import com.insightfullogic.honest_profiler.framework.generator.FlatGenerator;
import com.insightfullogic.honest_profiler.framework.scenario.SimplifiedLogScenario;

@RunWith(Parameterized.class)
public class FlatProfileAggregatorTest
{
    @Parameters(name = "{0} : <{1},{2}>")
    public static Collection<Object[]> data()
    {
        return getScenariosAndGroupings();
    }

    // Instance Properties

    private SimplifiedLogScenario scenario;
    private ThreadGrouping threadGrouping;
    private FrameGrouping frameGrouping;

    // Instance Constructors

    public FlatProfileAggregatorTest(SimplifiedLogScenario scenario,
                                     ThreadGrouping threadGrouping,
                                     FrameGrouping frameGrouping)
    {
        this.scenario = scenario;
        this.threadGrouping = threadGrouping;
        this.frameGrouping = frameGrouping;
    }

    // Actual Test Method

    @Test
    public void testScenario()
    {
        FlatGenerator gen;

        gen = new FlatGenerator(threadGrouping, frameGrouping);
        scenario.executeAndEnd(gen);
        scenario.checkLinearAggregation(new FlatCheckAdapter(gen.getFlat()));
    }
}
