/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.aliyuncs.rds.model.v20140815;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.rds.transform.v20140815.DescribeRegionsTestResponseUnmarshaller;
import com.aliyuncs.transform.UnmarshallerContext;

import java.util.List;

/**
 * @author auto create
 */
public class DescribeRegionsTestResponse extends AcsResponse {

    private String requestId;

    private List<RDSRegion> regions;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<RDSRegion> getRegions() {
        return this.regions;
    }

    public void setRegions(List<RDSRegion> regions) {
        this.regions = regions;
    }

    public static class RDSRegion {

        private String regionId;

        private String zoneId;

        public String getRegionId() {
            return this.regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getZoneId() {
            return this.zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }
    }

    @Override
    public DescribeRegionsTestResponse getInstance(UnmarshallerContext context) {
        return DescribeRegionsTestResponseUnmarshaller.unmarshall(this, context);
    }
}
