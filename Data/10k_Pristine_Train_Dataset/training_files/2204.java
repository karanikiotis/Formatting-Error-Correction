/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.ocaml;

import com.facebook.buck.io.BuildCellRelativePath;
import com.facebook.buck.log.Logger;
import com.facebook.buck.rules.AbstractBuildRuleWithDeclaredAndExtraDeps;
import com.facebook.buck.rules.AddToRuleKey;
import com.facebook.buck.rules.BuildContext;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildableContext;
import com.facebook.buck.rules.SourcePath;
import com.facebook.buck.step.Step;
import com.facebook.buck.step.fs.MakeCleanDirectoryStep;
import com.google.common.collect.ImmutableList;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nullable;

/** A build rule which cleans a target's build output folder. */
public class OcamlClean extends AbstractBuildRuleWithDeclaredAndExtraDeps {

  @AddToRuleKey private final OcamlBuildContext ocamlContext;

  private static final Logger LOG = Logger.get(OcamlClean.class);

  public OcamlClean(BuildRuleParams params, OcamlBuildContext ocamlContext) {
    super(params);
    this.ocamlContext = ocamlContext;
  }

  @Override
  public ImmutableList<Step> getBuildSteps(
      BuildContext context, BuildableContext buildableContext) {

    ImmutableList.Builder<Step> steps = ImmutableList.builder();

    Path bcDir = ocamlContext.getCompileBytecodeOutputDir();
    Path optDir = ocamlContext.getCompileNativeOutputDir();

    if (Files.exists(bcDir)) {
      buildableContext.recordArtifact(bcDir);
      LOG.debug("Adding clean step for bytecode output dir %s", bcDir.toString());

      steps.addAll(
          MakeCleanDirectoryStep.of(
              BuildCellRelativePath.fromCellRelativePath(
                  context.getBuildCellRootPath(), getProjectFilesystem(), bcDir)));
    }

    if (Files.exists(optDir)) {
      buildableContext.recordArtifact(optDir);
      LOG.debug("Adding clean step for native output dir %s", optDir.toString());

      steps.addAll(
          MakeCleanDirectoryStep.of(
              BuildCellRelativePath.fromCellRelativePath(
                  context.getBuildCellRootPath(), getProjectFilesystem(), optDir)));
    }
    return steps.build();
  }

  @Nullable
  @Override
  public SourcePath getSourcePathToOutput() {
    return null;
  }

  @Override
  public boolean isCacheable() {
    return false;
  }
}
