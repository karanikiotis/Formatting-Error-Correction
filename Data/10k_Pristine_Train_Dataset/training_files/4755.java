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
package com.facebook.presto.sql.planner.iterative.rule;

import com.facebook.presto.Session;
import com.facebook.presto.sql.planner.PlanNodeIdAllocator;
import com.facebook.presto.sql.planner.SymbolAllocator;
import com.facebook.presto.sql.planner.iterative.Lookup;
import com.facebook.presto.sql.planner.iterative.Pattern;
import com.facebook.presto.sql.planner.iterative.Rule;
import com.facebook.presto.sql.planner.plan.FilterNode;
import com.facebook.presto.sql.planner.plan.PlanNode;

import java.util.Optional;

import static com.facebook.presto.sql.ExpressionUtils.combineConjuncts;

public class MergeFilters
    implements Rule
{
    private static final Pattern PATTERN = Pattern.node(FilterNode.class);

    @Override
    public Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    public Optional<PlanNode> apply(PlanNode node, Lookup lookup, PlanNodeIdAllocator idAllocator, SymbolAllocator symbolAllocator, Session session)
    {
        FilterNode parent = (FilterNode) node;

        PlanNode source = lookup.resolve(parent.getSource());
        if (!(source instanceof FilterNode)) {
            return Optional.empty();
        }

        FilterNode child = (FilterNode) source;

        return Optional.of(
                new FilterNode(
                        parent.getId(),
                        child.getSource(),
                        combineConjuncts(child.getPredicate(), parent.getPredicate())));
    }
}
