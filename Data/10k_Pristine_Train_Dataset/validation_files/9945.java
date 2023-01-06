/*
 * Copyright © 2017 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.cdap.master.startup;

import co.cask.cdap.common.conf.CConfiguration;
import co.cask.cdap.common.conf.Constants;
import co.cask.cdap.common.startup.Check;
import co.cask.cdap.explore.service.ExploreServiceUtils;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks that Hive version on the cluster is supported.
 */
// class is picked up through classpath examination
@SuppressWarnings("unused")
public class HiveCheck extends Check {
  private static final Logger LOG = LoggerFactory.getLogger(HiveCheck.class);
  private final CConfiguration cConf;

  @Inject
  private HiveCheck(CConfiguration cConf) {
    this.cConf = cConf;
  }

  @Override
  public void run() throws Exception {
    if (cConf.getBoolean(Constants.Explore.EXPLORE_ENABLED)) {
      ExploreServiceUtils.checkHiveSupport(cConf);
    } else {
      LOG.debug("Explore is disabled, skipping Hive support check.");
    }
  }
}
