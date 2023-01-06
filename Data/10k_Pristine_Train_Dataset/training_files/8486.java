// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License

package com.google.devtools.build.lib.rules.config;

import com.google.devtools.build.lib.packages.ClassObjectConstructor;
import com.google.devtools.build.lib.skylarkinterface.SkylarkCallable;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModule;

/**
 * Skylark namespace used to interact with Blaze's configurability APIs.
 */
@SkylarkModule(
  name = "config_common",
  doc = "Functions for Skylark to interact with Blaze's configurability APIs."
)
public class ConfigSkylarkCommon {
  @SkylarkCallable(
    name = ConfigFeatureFlagProvider.SKYLARK_NAME,
    doc = "The key used to retrieve the provider containing config_feature_flag's value.",
    structField = true
  )
  public ClassObjectConstructor getConfigFeatureFlagProviderConstructor() {
    return ConfigFeatureFlagProvider.SKYLARK_CONSTRUCTOR;
  }
}
