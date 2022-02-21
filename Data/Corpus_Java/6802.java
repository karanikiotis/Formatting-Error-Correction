/*
 * Copyright © 2016 Cask Data, Inc.
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

package co.cask.cdap.etl.mock.transform;

import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.api.plugin.PluginProperties;
import co.cask.cdap.etl.api.Arguments;
import co.cask.cdap.etl.api.Lookup;
import co.cask.cdap.etl.api.LookupProvider;
import co.cask.cdap.etl.api.StageMetrics;
import co.cask.cdap.etl.api.TransformContext;
import co.cask.cdap.etl.mock.common.MockArguments;
import co.cask.cdap.etl.mock.common.MockLookupProvider;
import co.cask.cdap.etl.mock.common.MockStageMetrics;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Mock context for unit tests
 */
public class MockTransformContext implements TransformContext {
  private final PluginProperties pluginProperties;
  private final MockStageMetrics metrics;
  private final LookupProvider lookup;
  private final String stageName;
  private final Arguments arguments;

  public MockTransformContext() {
    this("someStage");
  }

  public MockTransformContext(String stageName) {
    this(stageName, new HashMap<String, String>());
  }

  public MockTransformContext(String stageName, Map<String, String> args) {
    this(stageName, args, new MockLookupProvider(null));
  }

  public MockTransformContext(String stageName, Map<String, String> args, LookupProvider lookup) {
    this.pluginProperties = PluginProperties.builder().addAll(args).build();
    this.lookup = lookup;
    this.metrics = new MockStageMetrics(stageName);
    this.stageName = stageName;
    this.arguments = new MockArguments(args);
  }

  @Override
  public PluginProperties getPluginProperties() {
    return pluginProperties;
  }

  @Override
  public PluginProperties getPluginProperties(String pluginId) {
    return null;
  }

  @Override
  public StageMetrics getMetrics() {
    return metrics;
  }

  public MockStageMetrics getMockMetrics() {
    return metrics;
  }

  @Override
  public String getStageName() {
    return stageName;
  }

  @Override
  public <T> T newPluginInstance(String pluginId) throws InstantiationException {
    return null;
  }

  @Nullable
  @Override
  public Schema getInputSchema() {
    return null;
  }

  @Override
  public Map<String, Schema> getInputSchemas() {
    return Collections.emptyMap();
  }

  @Nullable
  @Override
  public Schema getOutputSchema() {
    return null;
  }

  @Override
  public Arguments getArguments() {
    return arguments;
  }

  @Override
  public <T> Class<T> loadPluginClass(String pluginId) {
    return null;
  }

  @Override
  public <T> Lookup<T> provide(String table, Map<String, String> arguments) {
    return lookup.provide(table, arguments);
  }

  @Nullable
  @Override
  public URL getServiceURL(String applicationId, String serviceId) {
    //no-op
    return null;
  }

  @Nullable
  @Override
  public URL getServiceURL(String serviceId) {
    //no-op
    return null;
  }
}
