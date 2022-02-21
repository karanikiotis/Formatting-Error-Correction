/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.v2.serviceinstances;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cloudfoundry.Nullable;
import org.immutables.value.Value;

import java.util.List;

/**
 * The entity response payload for any type of Service Instances
 */
@JsonDeserialize
@Value.Immutable
abstract class _UnionServiceInstanceEntity extends BaseServiceInstanceEntity {

    /**
     * The dashboard url
     */
    @JsonProperty("dashboard_url")
    @Nullable
    abstract String getDashboardUrl();

    /**
     * The gateway data
     */
    @Deprecated
    @JsonProperty("gateway_data")
    @Nullable
    abstract GatewayData getGatewayData();

    /**
     * The last operation
     */
    @JsonProperty("last_operation")
    @Nullable
    abstract LastOperation getLastOperation();

    /**
     * URL to which requests for bound routes will be forwarded
     */
    @JsonProperty("route_service_url")
    @Nullable
    abstract String getRouteServiceUrl();

    /**
     * The service id
     */
    @JsonProperty("service_guid")
    @Nullable
    abstract String getServiceId();

    /**
     * The service keys url
     */
    @JsonProperty("service_keys_url")
    @Nullable
    abstract String getServiceKeysUrl();

    /**
     * The service plan id
     */
    @JsonProperty("service_plan_guid")
    @Nullable
    abstract String getServicePlanId();

    /**
     * The service plan url
     */
    @JsonProperty("service_plan_url")
    @Nullable
    abstract String getServicePlanUrl();

    /**
     * The service url
     */
    @JsonProperty("service_url")
    @Nullable
    abstract String getServiceUrl();

    /**
     * The url for the syslog_drain to direct to
     */
    @JsonProperty("syslog_drain_url")
    @Nullable
    abstract String getSyslogDrainUrl();

    /**
     * The tags
     */
    @JsonProperty("tags")
    @Nullable
    abstract List<String> getTags();

}
