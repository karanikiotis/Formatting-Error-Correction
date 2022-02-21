/*
 * Copyright 2003-2015 JetBrains s.r.o.
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
package jetbrains.mps.generator.impl;

import jetbrains.mps.extapi.model.GeneratableSModel;
import jetbrains.mps.generator.GenerationOptions;
import jetbrains.mps.generator.GenerationParametersProvider;
import jetbrains.mps.generator.IncrementalGenerationStrategy;
import jetbrains.mps.generator.ModelDigestUtil;
import jetbrains.mps.generator.impl.cache.IntermediateCacheHelper;
import jetbrains.mps.generator.impl.dependencies.DependenciesBuilder;
import jetbrains.mps.generator.impl.dependencies.GenerationDependencies;
import jetbrains.mps.generator.impl.dependencies.GenerationRootDependencies;
import jetbrains.mps.generator.impl.dependencies.IncrementalDependenciesBuilder;
import jetbrains.mps.generator.impl.dependencies.NonIncrementalDependenciesBuilder;
import jetbrains.mps.generator.impl.plan.ConnectedComponentPartitioner;
import jetbrains.mps.generator.impl.plan.ConnectedComponentPartitioner.Component;
import jetbrains.mps.util.IterableUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.mps.openapi.model.EditableSModel;
import org.jetbrains.mps.openapi.model.SModel;
import org.jetbrains.mps.openapi.model.SNode;
import org.jetbrains.mps.openapi.module.SRepository;
import org.jetbrains.mps.openapi.persistence.PersistenceFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Evgeny Gryaznov, Jun 3, 2010
 */
public class IncrementalGenerationHandler {
  private static final String CONDITIONALS_ID = "";

  private final SModel myModel;
  private final SRepository myRepository;
  private GenerationOptions myGenerationOptions;
  private final String myParametersHash;
  private final IncrementalReporter myTracer;
  private Set<SNode> myUnchangedRoots;
  private Set<SNode> myRequiredRoots;
  private boolean myConditionalsUnchanged;
  private boolean myConditionalsRequired;
  private int myRootsCount;
  private Map<String, String> myGenerationHashes;
  private GenerationDependencies mySavedDependencies;
  private final IntermediateCacheHelper myCacheHelper;

  public IncrementalGenerationHandler(SModel model, @NotNull SRepository repository, GenerationOptions options,
      @NotNull IntermediateCacheHelper cacheHelper, IncrementalReporter tracer) {
    myModel = model;
    myRepository = repository;
    myGenerationOptions = options;
    final GenerationParametersProvider parametersProvider = options.getParametersProvider();
    if (parametersProvider == null) {
      myParametersHash = null;
    } else {
      myParametersHash = getParametersHash(parametersProvider.getParameters(model));
    }
    myTracer = tracer;
    myUnchangedRoots = Collections.emptySet();
    myRequiredRoots = Collections.emptySet();
    myConditionalsUnchanged = false;
    myConditionalsRequired = false;
    myCacheHelper = cacheHelper;
    init();
  }

  private void init() {
    IncrementalGenerationStrategy incrementalStrategy = myGenerationOptions.getIncrementalStrategy();

    myGenerationHashes = incrementalStrategy.getModelHashes(myModel, null);

    if (myGenerationOptions.isRebuildAll() || !incrementalStrategy.isIncrementalEnabled()) {
      return;
    }

    GenerationDependencies dependencies = incrementalStrategy.getDependencies(myModel);
    if (dependencies == null || !dependencies.isContainsIncrementalInfo() || myGenerationHashes == null) {
      if (myTracer != null) {
        if (dependencies == null) {
          myTracer.report("No dependencies data found");
        }
        if (myGenerationHashes == null) {
          myTracer.report("No caches data for input model");
        }
      }
      return;
    }

    myCacheHelper.loadExisting(myModel, myTracer);
    if (!myCacheHelper.hasCache() && incrementalStrategy.getContainer() != null) {
      // if we are creating a new cache without the previous one => rebuild all
      return;
    }

    // trying to optimize
    analyzeDependencies(dependencies);
    if (canOptimize()) {
      mySavedDependencies = dependencies;
    }
  }

  public boolean canOptimize() {
    return !myUnchangedRoots.isEmpty() || myConditionalsUnchanged ||
        !myRequiredRoots.isEmpty() || myConditionalsRequired;
  }

  public Set<SNode> getIgnoredRoots() {
    return Collections.unmodifiableSet(myUnchangedRoots);
  }

  public Set<SNode> getRequiredRoots() {
    return Collections.unmodifiableSet(myRequiredRoots);
  }

  public boolean canIgnoreConditionals() {
    return myConditionalsUnchanged;
  }

  public boolean requireConditionals() {
    return myConditionalsRequired;
  }

  public int getRootsCount() {
    return myRootsCount;
  }

  private void analyzeDependencies(@NotNull GenerationDependencies oldDependencies) {

    GenerationRootDependencies commonDeps = oldDependencies.getDependenciesFor(GeneratableSModel.HEADER);
    if (commonDeps == null) {
      if (myTracer != null) myTracer.report("Dependencies are incomplete. No info about header.");
      return;
    }

    // check model header, rebuild all if changed
    {
      String oldHash = commonDeps.getHash();
      String newHash = myGenerationHashes.get(GeneratableSModel.HEADER);
      if (oldHash == null || newHash == null || !newHash.equals(oldHash)) {
        if (myTracer != null) myTracer.report("Changes in model header, regenerating.");
        return;
      }
    }

    // check generation parameters, rebuild all if changed
    {
      String oldHash = oldDependencies.getParametersHash();
      String newHash = myParametersHash;
      if (oldHash == null
          ? (newHash != null)
          : !oldHash.equals(newHash)) {
        if (myTracer != null) myTracer.report("Changes in generation parameters, regenerating.");
        return;
      }
    }

    // collect changed models
    Set<String> changedModels = new HashSet<String>();
    Map<String, String> externalHashes = oldDependencies.getExternalHashes();
    for (Entry<String, String> entry : externalHashes.entrySet()) {
      String modelReference = entry.getKey();
      SModel sm = PersistenceFacade.getInstance().createModelReference(modelReference).resolve(myRepository);
      if (sm == null) {
        changedModels.add(modelReference);
        continue;
      }
      String oldHash = entry.getValue();
      if (oldHash == null) {
        // TODO hash for packaged models
        if ((sm instanceof EditableSModel) && !sm.isReadOnly()) {
          changedModels.add(modelReference);
        }
        continue;
      }
      Map<String, String> map = myGenerationOptions.getIncrementalStrategy().getModelHashes(sm, null);
      String newHash = map != null ? map.get(GeneratableSModel.FILE) : null;
      if (newHash == null || !oldHash.equals(newHash)) {
        changedModels.add(modelReference);
      }
    }

    // collect unchanged roots (same hash; external dependencies are unchanged)
    SModel smodel = myModel;
    myRootsCount = IterableUtil.asCollection(smodel.getRootNodes()).size();

    myUnchangedRoots = new HashSet<SNode>();
    for (SNode root : smodel.getRootNodes()) {
      String id = root.getNodeId().toString();
      GenerationRootDependencies rd = oldDependencies.getDependenciesFor(id);
      String oldHash;
      if (rd == null || (oldHash = rd.getHash()) == null) continue;

      String newHash = myGenerationHashes.get(id);
      if (newHash == null || !newHash.equals(oldHash)) continue;

      boolean isDirty = false;
      for (String m : rd.getExternal()) {
        if (changedModels.contains(m)) {
          isDirty = true;
          break;
        }
      }
      if (!isDirty) {
        myUnchangedRoots.add(root);
      }
    }

    // there are dirty roots -> mark unchanged roots which dependsOnModelNodes as dirty
    if (myUnchangedRoots.size() < myRootsCount) {
      Iterator<SNode> it = myUnchangedRoots.iterator();
      while (it.hasNext()) {
        SNode root = it.next();
        String id = root.getNodeId().toString();
        GenerationRootDependencies rd = oldDependencies.getDependenciesFor(id);
        if (rd.isDependsOnNodes()) {
          it.remove();
        }
      }
    }

    // all roots are dirty? rebuild all
    if (myUnchangedRoots.isEmpty()) {
      if (myTracer != null) myTracer.report("All roots are dirty.");
      return;
    }

    myConditionalsUnchanged = (myUnchangedRoots.size() == myRootsCount || !commonDeps.isDependsOnNodes());

    // calculate which unchanged roots should be re-generated according with
    // saved dependencies and references between roots

    Map<String, Set<String>> savedDep = getDependencies(oldDependencies, myUnchangedRoots, myConditionalsUnchanged);

    ConnectedComponentPartitioner partitioner = null;
    boolean changed;

    ArrayList<SNode> roots = new ArrayList<SNode>();
    for (SNode root : smodel.getRootNodes()) {
      roots.add(root);
    }

    // Phase 1: build closure using strongly connected components (only if we have cache)
    if (myCacheHelper.hasCache()) {
      closureUsingSavedDependencies(savedDep);

      if (myUnchangedRoots.isEmpty() && !myConditionalsUnchanged) {
        return;
      }

      partitioner = new ConnectedComponentPartitioner(roots);
      Component[] strongComponents = partitioner.partitionStrong();
      changed = closureUsingStrongComponents(strongComponents, savedDep);

      // repeat
      while (changed) {
        if (myUnchangedRoots.isEmpty() && !myConditionalsUnchanged) {
          return;
        }
        changed = closureUsingSavedDependencies(savedDep);
        if (changed) {
          changed = closureUsingStrongComponents(strongComponents, savedDep);
        }
      }

      // at this point dirty component can depend on "clean" component: we need to
      // load "clean" component roots from cache
      myRequiredRoots = new HashSet<SNode>(myUnchangedRoots);
      myConditionalsRequired = myConditionalsUnchanged;
    }


    // Phase 2: build closure using connected components
    addIncomingDependencies(oldDependencies, savedDep);
    closureUsingSavedDependencies(savedDep);

    if (myUnchangedRoots.isEmpty() && !myConditionalsUnchanged) {
      return;
    }

    // closure using current dependencies
    if (partitioner == null) {
      partitioner = new ConnectedComponentPartitioner(roots);
    }
    List<SNode[]> components = partitioner.partition();
    changed = closureUsingReferences(components, savedDep);

    // repeat
    while (changed) {
      if (myUnchangedRoots.isEmpty() && !myConditionalsUnchanged) {
        return;
      }
      changed = closureUsingSavedDependencies(savedDep);
      if (changed) {
        changed = closureUsingReferences(components, savedDep);
      }
    }

    // at this point unchanged roots can be excluded from generation at all (there is no
    // references/dependency between them and dirty roots)

    if (!myRequiredRoots.isEmpty()) {
      myRequiredRoots.removeAll(myUnchangedRoots);
      if (myConditionalsUnchanged) {
        myConditionalsRequired = false;
      }
    }
  }

  /*
   *
   */
  private boolean closureUsingReferences(List<SNode[]> components, Map<String, Set<String>> dep) {
    boolean result = false;
    for (SNode[] component : components) {
      boolean hasUnchanged = false;
      boolean hasChanged = false;
      for (SNode n : component) {
        if (myUnchangedRoots.contains(n)) {
          hasUnchanged = true;
        } else {
          hasChanged = true;
        }
      }
      if (hasUnchanged && hasChanged) {
        for (SNode n : component) {
          myUnchangedRoots.remove(n);
          dep.remove(n.getNodeId().toString());
          result = true;
        }
      }
    }
    return result;
  }

  /*
   *  1. all roots in a single component should have the same dirty state
   *  2. unchanged component which has dependency on dirty component is marked as dirty
   *
   *  components array is topologically sorted
   */
  private boolean closureUsingStrongComponents(Component[] components, Map<String, Set<String>> dep) {
    boolean result = false;
    for (Component component : components) {
      boolean hasUnchanged = false;
      boolean hasChanged = false;
      for (SNode n : component.getRoots()) {
        if (myUnchangedRoots.contains(n)) {
          hasUnchanged = true;
        } else {
          hasChanged = true;
        }
      }
      for (Component c : component.getDependsOn()) {
        if (c.isDirty()) {
          hasChanged = true;
        }
      }
      if (hasUnchanged && hasChanged) {
        for (SNode n : component.getRoots()) {
          if (myUnchangedRoots.remove(n)) {
            dep.remove(n.getNodeId().toString());
            result = true;
          }
        }
      }
      component.setDirty(hasChanged);
    }
    return result;
  }

  /*
   *  unchanged root which has dependency on dirty root is marked as dirty
   */
  private boolean closureUsingSavedDependencies(Map<String, Set<String>> dep) {
    boolean result = false;
    boolean changed = true;
    while (changed) {
      changed = false;
      Iterator<SNode> it = myUnchangedRoots.iterator();
      while (it.hasNext()) {
        SNode root = it.next();
        Set<String> rootDeps = dep.get(root.getNodeId().toString());
        boolean dirty = false;
        for (String localRootId : rootDeps) {
          if (!dep.containsKey(localRootId)) {
            dirty = true;
            break;
          }
        }
        if (dirty) {
          it.remove();
          dep.remove(root.getNodeId().toString());
          changed = true;
        }
      }
      if (myConditionalsUnchanged) {
        Set<String> conditionalsDeps = dep.get(CONDITIONALS_ID);
        for (String localRootId : conditionalsDeps) {
          if (!dep.containsKey(localRootId)) {
            dep.remove(CONDITIONALS_ID);
            myConditionalsUnchanged = false;
            changed = true;
            break;
          }
        }
      }
      result |= changed;
    }
    return result;
  }

  private static void addIncomingDependencies(GenerationDependencies dependencies, Map<String, Set<String>> graph) {
    for (GenerationRootDependencies rd : dependencies.getRootDependencies()) {
      String id = rd.getRootId();
      if (id == null) {
        id = CONDITIONALS_ID;
      }
      // reversed
      if (rd.isDependsOnConditionals()) {
        Set<String> r = graph.get(CONDITIONALS_ID);
        if (r != null) {
          r.add(id);
        }
      }
      for (String s : rd.getLocal()) {
        Set<String> r = graph.get(s);
        if (r != null) {
          r.add(id);
        }
      }
    }
  }

  private static Map<String, Set<String>> getDependencies(GenerationDependencies dependencies, Set<SNode> selectedRoots, boolean condUnchanged) {
    Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
    for (SNode n : selectedRoots) {
      graph.put(n.getNodeId().toString(), new HashSet<String>());
    }
    if (condUnchanged) {
      graph.put(CONDITIONALS_ID, new HashSet<String>());
    }
    for (GenerationRootDependencies rd : dependencies.getRootDependencies()) {
      String id = rd.getRootId();
      if (id == null) {
        id = CONDITIONALS_ID;
      }
      Set<String> currentDeps = graph.get(id);
      if (currentDeps != null) {
        currentDeps.addAll(rd.getLocal());
        if (rd.isDependsOnConditionals()) {
          currentDeps.add(CONDITIONALS_ID);
        }
      }
    }
    return graph;
  }

  public SModel getModel() {
    return myModel;
  }

  private static String getParametersHash(Map<String, Object> parameters) {
    if (parameters == null || parameters.isEmpty()) {
      return null;
    }
    String val = (String) parameters.get(GenerationParametersProvider.HASH);
    if (val != null) {
      return val;
    }

    StringBuilder sb = new StringBuilder();
    String[] keys = parameters.keySet().toArray(new String[parameters.size()]);
    Arrays.sort(keys);
    for (String k : keys) {
      sb.append(k);
      sb.append(':');
      sb.append(String.valueOf(parameters.get(k)));
      sb.append(";\n");
    }
    return ModelDigestUtil.hashText(sb.toString());
  }

  public DependenciesBuilder createDependenciesBuilder() {
    IncrementalGenerationStrategy incrementalStrategy = myGenerationOptions.getIncrementalStrategy();
    if (!incrementalStrategy.isIncrementalEnabled()) {
      return new NonIncrementalDependenciesBuilder(myGenerationHashes, myParametersHash);
    }

    IncrementalDependenciesBuilder result = new IncrementalDependenciesBuilder(myModel, myGenerationHashes, myParametersHash, myCacheHelper);
    result.propagateDependencies(myUnchangedRoots, myRequiredRoots, myConditionalsUnchanged, myConditionalsRequired, mySavedDependencies);
    return result;
  }

  public interface IncrementalReporter {
    void report(String message);
  }
}
