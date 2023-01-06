// Copyright 2014 The Bazel Authors. All rights reserved.
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
// limitations under the License.
package com.google.devtools.build.lib.rules.java;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.analysis.ConfiguredTarget;
import com.google.devtools.build.lib.analysis.OutputGroupProvider;
import com.google.devtools.build.lib.analysis.RuleConfiguredTargetBuilder;
import com.google.devtools.build.lib.analysis.RuleContext;
import com.google.devtools.build.lib.analysis.RunfilesProvider;
import com.google.devtools.build.lib.analysis.TransitiveInfoCollection;
import com.google.devtools.build.lib.collect.nestedset.NestedSet;
import com.google.devtools.build.lib.collect.nestedset.NestedSetBuilder;
import com.google.devtools.build.lib.collect.nestedset.Order;
import com.google.devtools.build.lib.rules.RuleConfiguredTargetFactory;
import com.google.devtools.build.lib.rules.cpp.CcLinkParams;
import com.google.devtools.build.lib.rules.cpp.CcLinkParamsProvider;
import com.google.devtools.build.lib.rules.cpp.CcLinkParamsStore;
import com.google.devtools.build.lib.rules.cpp.CppCompilationContext;
import com.google.devtools.build.lib.rules.cpp.LinkerInput;
import com.google.devtools.build.lib.rules.java.JavaCompilationArgs.ClasspathType;
import com.google.devtools.build.lib.rules.java.proto.GeneratedExtensionRegistryProvider;

/**
 * Implementation for the java_library rule.
 */
public class JavaLibrary implements RuleConfiguredTargetFactory {
  private final JavaSemantics semantics;

  protected JavaLibrary(JavaSemantics semantics) {
    this.semantics = semantics;
  }

  @Override
  public ConfiguredTarget create(RuleContext ruleContext)
      throws InterruptedException, RuleErrorException {
    JavaCommon common = new JavaCommon(ruleContext, semantics);
    RuleConfiguredTargetBuilder builder =
        init(ruleContext, common, false /* includeGeneratedExtensionRegistry */);
    return builder != null ? builder.build() : null;
  }

  final RuleConfiguredTargetBuilder init(
      RuleContext ruleContext, final JavaCommon common, boolean includeGeneratedExtensionRegistry)
      throws InterruptedException {
    JavaTargetAttributes.Builder attributesBuilder = common.initCommon();

    // Collect the transitive dependencies.
    JavaCompilationHelper helper = new JavaCompilationHelper(
        ruleContext, semantics, common.getJavacOpts(), attributesBuilder);
    helper.addLibrariesToAttributes(common.targetsTreatedAsDeps(ClasspathType.COMPILE_ONLY));

    if (ruleContext.hasErrors()) {
      return null;
    }

    semantics.checkRule(ruleContext, common);

    JavaCompilationArtifacts.Builder javaArtifactsBuilder = new JavaCompilationArtifacts.Builder();

    if (ruleContext.hasErrors()) {
      common.setJavaCompilationArtifacts(JavaCompilationArtifacts.EMPTY);
      return null;
    }

    JavaConfiguration javaConfig = ruleContext.getFragment(JavaConfiguration.class);
    NestedSetBuilder<Artifact> filesBuilder = NestedSetBuilder.stableOrder();

    JavaTargetAttributes attributes = helper.getAttributes();
    if (attributes.hasMessages()) {
      helper.setTranslations(
          semantics.translate(ruleContext, javaConfig, attributes.getMessages()));
    }

    ruleContext.checkSrcsSamePackage(true);

    Artifact jar = null;

    Artifact srcJar = ruleContext.getImplicitOutputArtifact(
        JavaSemantics.JAVA_LIBRARY_SOURCE_JAR);

    NestedSet<Artifact> transitiveSourceJars = common.collectTransitiveSourceJars(srcJar);
    JavaSourceJarsProvider.Builder sourceJarsProviderBuilder =
        JavaSourceJarsProvider.builder()
            .addSourceJar(srcJar)
            .addAllTransitiveSourceJars(transitiveSourceJars);

    Artifact classJar = ruleContext.getImplicitOutputArtifact(
        JavaSemantics.JAVA_LIBRARY_CLASS_JAR);

    if (attributes.hasSources() || attributes.hasResources()) {
      // We only want to add a jar to the classpath of a dependent rule if it has content.
      javaArtifactsBuilder.addRuntimeJar(classJar);
      jar = classJar;
    }

    filesBuilder.add(classJar);

    Artifact manifestProtoOutput = helper.createManifestProtoOutput(classJar);

    // The gensrc jar is created only if the target uses annotation processing.
    // Otherwise, it is null, and the source jar action will not depend on the compile action.
    Artifact genSourceJar = null;
    Artifact genClassJar = null;
    if (helper.usesAnnotationProcessing()) {
      genClassJar = helper.createGenJar(classJar);
      genSourceJar = helper.createGensrcJar(classJar);
      helper.createGenJarAction(classJar, manifestProtoOutput, genClassJar);
    }

    Artifact outputDepsProto = helper.createOutputDepsProtoArtifact(classJar, javaArtifactsBuilder);

    helper.createCompileActionWithInstrumentation(classJar, manifestProtoOutput, genSourceJar,
        outputDepsProto, javaArtifactsBuilder);
    helper.createSourceJarAction(srcJar, genSourceJar);

    Artifact iJar = null;
    if (attributes.hasSources() && jar != null) {
      iJar = helper.createCompileTimeJarAction(jar, javaArtifactsBuilder);
    }
    JavaRuleOutputJarsProvider.Builder ruleOutputJarsProviderBuilder =
        JavaRuleOutputJarsProvider.builder()
            .addOutputJar(classJar, iJar, ImmutableList.of(srcJar))
            .setJdeps(outputDepsProto);

    GeneratedExtensionRegistryProvider generatedExtensionRegistryProvider = null;
    if (includeGeneratedExtensionRegistry) {
      generatedExtensionRegistryProvider =
          semantics.createGeneratedExtensionRegistry(
              ruleContext,
              common,
              filesBuilder,
              javaArtifactsBuilder,
              ruleOutputJarsProviderBuilder,
              sourceJarsProviderBuilder);
    }

    boolean neverLink = JavaCommon.isNeverLink(ruleContext);
    JavaCompilationArtifacts javaArtifacts = javaArtifactsBuilder.build();
    common.setJavaCompilationArtifacts(javaArtifacts);
    common.setClassPathFragment(
        new ClasspathConfiguredFragment(
            javaArtifacts, attributes, neverLink, helper.getBootclasspathOrDefault()));
    CppCompilationContext transitiveCppDeps = common.collectTransitiveCppDeps();

    // If sources are empty, treat this library as a forwarding node for dependencies.
    JavaCompilationArgs javaCompilationArgs =
        common.collectJavaCompilationArgs(false, neverLink, false);
    JavaCompilationArgs recursiveJavaCompilationArgs =
        common.collectJavaCompilationArgs(true, neverLink, false);
    NestedSet<Artifact> compileTimeJavaDepArtifacts = common.collectCompileTimeDependencyArtifacts(
        javaArtifacts.getCompileTimeDependencyArtifact());
    NestedSet<Artifact> runTimeJavaDepArtifacts = NestedSetBuilder.emptySet(Order.STABLE_ORDER);
    NestedSet<LinkerInput> transitiveJavaNativeLibraries =
        common.collectTransitiveJavaNativeLibraries();

    CcLinkParamsStore ccLinkParamsStore = new CcLinkParamsStore() {
      @Override
      protected void collect(CcLinkParams.Builder builder, boolean linkingStatically,
                             boolean linkShared) {
        builder.addTransitiveTargets(common.targetsTreatedAsDeps(ClasspathType.BOTH),
            JavaCcLinkParamsProvider.TO_LINK_PARAMS, CcLinkParamsProvider.TO_LINK_PARAMS);
      }
    };

    ProtoJavaApiInfoAspectProvider.Builder protoAspectBuilder =
        ProtoJavaApiInfoAspectProvider.builder();
    for (TransitiveInfoCollection dep : common.getDependencies()) {
      ProtoJavaApiInfoAspectProvider protoProvider =
          JavaProvider.getProvider(ProtoJavaApiInfoAspectProvider.class, dep);
      if (protoProvider != null) {
        protoAspectBuilder.addTransitive(protoProvider);
      }
    }

    RuleConfiguredTargetBuilder builder =
        new RuleConfiguredTargetBuilder(ruleContext);

    semantics.addProviders(
        ruleContext, common, ImmutableList.<String>of(), classJar, srcJar,
        genClassJar, genSourceJar, ImmutableMap.<Artifact, Artifact>of(),
        filesBuilder, builder);
    if (generatedExtensionRegistryProvider != null) {
      builder.add(GeneratedExtensionRegistryProvider.class, generatedExtensionRegistryProvider);
    }

    JavaCompilationArgsProvider compilationArgsProvider =
        JavaCompilationArgsProvider.create(
            javaCompilationArgs, recursiveJavaCompilationArgs,
            compileTimeJavaDepArtifacts, runTimeJavaDepArtifacts);
    JavaSourceJarsProvider sourceJarsProvider = sourceJarsProviderBuilder.build();
    JavaRuleOutputJarsProvider ruleOutputJarsProvider = ruleOutputJarsProviderBuilder.build();

    NestedSet<Artifact> filesToBuild = filesBuilder.build();
    common.addTransitiveInfoProviders(builder, filesToBuild, classJar);
    common.addGenJarsProvider(builder, genClassJar, genSourceJar);

    NestedSet<Artifact> proguardSpecs = new ProguardLibrary(ruleContext).collectProguardSpecs();

    CcLinkParamsProvider ccLinkParamsProvider = new CcLinkParamsProvider(ccLinkParamsStore);
    JavaProvider javaProvider = JavaProvider.Builder.create()
        .addProvider(JavaCompilationArgsProvider.class, compilationArgsProvider)
        .addProvider(JavaSourceJarsProvider.class, sourceJarsProvider)
        .addProvider(ProtoJavaApiInfoAspectProvider.class, protoAspectBuilder.build())
        .addProvider(JavaRuleOutputJarsProvider.class, ruleOutputJarsProvider)
        // java_library doesn't need to return JavaRunfilesProvider
        .build();
    builder
        .addSkylarkTransitiveInfo(
            JavaSkylarkApiProvider.NAME, JavaSkylarkApiProvider.fromRuleContext())
        .addProvider(ruleOutputJarsProvider)
        .addProvider(new JavaRuntimeJarProvider(javaArtifacts.getRuntimeJars()))
        .addProvider(
            RunfilesProvider.simple(
                JavaCommon.getRunfiles(ruleContext, semantics, javaArtifacts, neverLink)))
        .setFilesToBuild(filesToBuild)
        .addProvider(new JavaNeverlinkInfoProvider(neverLink))
        .addProvider(transitiveCppDeps)
        .addProvider(JavaCompilationArgsProvider.class, compilationArgsProvider)
        .addProvider(javaProvider)
        .addProvider(ccLinkParamsProvider)
        .addNativeDeclaredProvider(ccLinkParamsProvider)
        .addNativeDeclaredProvider(javaProvider)
        .addProvider(new JavaNativeLibraryProvider(transitiveJavaNativeLibraries))
        .addProvider(JavaSourceInfoProvider.fromJavaTargetAttributes(attributes, semantics))
        // TODO(bazel-team): this should only happen for java_plugin
        .addProvider(JavaCommon.getTransitivePlugins(ruleContext))
        .addProvider(new ProguardSpecProvider(proguardSpecs))
        .addProvider(sourceJarsProvider)
        .addOutputGroup(JavaSemantics.SOURCE_JARS_OUTPUT_GROUP, transitiveSourceJars)
        .addOutputGroup(OutputGroupProvider.HIDDEN_TOP_LEVEL, proguardSpecs);

    if (ruleContext.hasErrors()) {
      return null;
    }

    return builder;
  }
}
