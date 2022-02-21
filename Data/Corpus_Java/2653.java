/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.common.repo;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import org.glowroot.common.util.Styles;

public interface AgentRollupRepository {

    List<AgentRollup> readAgentRollups() throws Exception;

    String readAgentRollupDisplay(String agentRollupId) throws Exception;

    boolean isAgent(String agentRollupId) throws Exception;

    @Value.Immutable
    @Styles.AllParameters
    interface AgentRollup {
        String id();
        String display();
        // cannot rely on empty children to determine since children could have been deleted
        boolean agent();
        @Nullable
        Date lastCaptureTime();
        List<AgentRollup> children();
    }
}
