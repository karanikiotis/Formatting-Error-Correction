/*
 * Copyright 2016-present Facebook, Inc.
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

package com.facebook.buck.cxx;

import com.facebook.buck.rules.BuildRule;
import com.facebook.buck.rules.RuleKeyAppendable;
import com.facebook.buck.rules.RuleKeyObjectSink;
import com.facebook.buck.rules.SourcePath;
import com.facebook.buck.rules.SourcePathResolver;
import com.facebook.buck.rules.SourcePathRuleFinder;
import com.facebook.buck.rules.args.Arg;
import com.facebook.buck.rules.args.StringArg;
import com.facebook.buck.util.RichStream;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import java.nio.file.Path;

/** Helper class for generating compiler invocations for a cxx compilation rule. */
class CompilerDelegate implements RuleKeyAppendable {
  // Fields that are added to rule key as is.
  private final Compiler compiler;

  // Fields that added to the rule key with some processing.
  private final CxxToolFlags compilerFlags;

  // Fields that are not added to the rule key.
  private final SourcePathResolver resolver;
  private final DebugPathSanitizer sanitizer;

  public CompilerDelegate(
      SourcePathResolver resolver,
      DebugPathSanitizer sanitizer,
      Compiler compiler,
      CxxToolFlags flags) {
    this.resolver = resolver;
    this.sanitizer = sanitizer;
    this.compiler = compiler;
    this.compilerFlags = flags;
  }

  @Override
  public void appendToRuleKey(RuleKeyObjectSink sink) {
    sink.setReflectively("compiler", compiler);
    sink.setReflectively("platformCompilerFlags", compilerFlags.getPlatformFlags());
    sink.setReflectively("ruleCompilerFlags", compilerFlags.getRuleFlags());
  }

  /** Returns the argument list for executing the compiler. */
  public ImmutableList<Arg> getCommand(CxxToolFlags prependedFlags, Path cellPath) {
    return ImmutableList.<Arg>builder()
        .addAll(StringArg.from(getCommandPrefix()))
        .addAll(getArguments(prependedFlags, cellPath))
        .build();
  }

  public ImmutableList<String> getCommandPrefix() {
    return compiler.getCommandPrefix(resolver);
  }

  public ImmutableList<Arg> getArguments(CxxToolFlags prependedFlags, Path cellPath) {
    return ImmutableList.<Arg>builder()
        .addAll(CxxToolFlags.concat(prependedFlags, compilerFlags).getAllFlags())
        .addAll(
            StringArg.from(
                compiler.getFlagsForReproducibleBuild(
                    sanitizer.getCompilationDirectory(), cellPath)))
        .build();
  }

  public CxxToolFlags getCompilerFlags() {
    return compilerFlags;
  }

  public ImmutableMap<String, String> getEnvironment() {
    return compiler.getEnvironment(resolver);
  }

  public ImmutableList<SourcePath> getInputsAfterBuildingLocally() {
    return Ordering.natural().immutableSortedCopy(compiler.getInputs());
  }

  public boolean isArgFileSupported() {
    return compiler.isArgFileSupported();
  }

  public boolean isDependencyFileSupported() {
    return compiler.isDependencyFileSupported();
  }

  public Compiler getCompiler() {
    return compiler;
  }

  public Iterable<BuildRule> getDeps(SourcePathRuleFinder ruleFinder) {
    ImmutableList.Builder<BuildRule> deps = ImmutableList.builder();
    deps.addAll(getCompiler().getDeps(ruleFinder));
    RichStream.from(getCompilerFlags().getAllFlags())
        .flatMap(a -> a.getDeps(ruleFinder).stream())
        .forEach(deps::add);
    return deps.build();
  }
}
