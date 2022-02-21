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

package jetbrains.mps.idea.core.project;

import com.intellij.notification.Notification;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.roots.RootProvider.RootSetChangedListener;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.VirtualFile;
import jetbrains.mps.classloading.IdeaPluginModuleFacet;
import jetbrains.mps.extapi.module.SRepositoryExt;
import jetbrains.mps.extapi.persistence.FileBasedModelRoot;
import jetbrains.mps.idea.core.project.stubs.JdkStubSolutionManager;
import jetbrains.mps.idea.core.project.stubs.StubModuleNameTakenException;
import jetbrains.mps.module.SDependencyImpl;
import jetbrains.mps.persistence.MementoImpl;
import jetbrains.mps.persistence.PersistenceRegistry;
import jetbrains.mps.persistence.java.library.JavaClassStubsModelRoot;
import jetbrains.mps.project.ModuleId;
import jetbrains.mps.project.Solution;
import jetbrains.mps.project.StubSolution;
import jetbrains.mps.project.structure.model.ModelRootDescriptor;
import jetbrains.mps.project.structure.modules.ModuleFacetDescriptor;
import jetbrains.mps.project.structure.modules.SolutionDescriptor;
import jetbrains.mps.smodel.BootstrapLanguages;
import jetbrains.mps.smodel.MPSModuleOwner;
import jetbrains.mps.smodel.ModuleRepositoryFacade;
import jetbrains.mps.vfs.FileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.mps.openapi.module.ModelAccess;
import org.jetbrains.mps.openapi.module.SDependency;
import org.jetbrains.mps.openapi.module.SDependencyScope;
import org.jetbrains.mps.openapi.module.SModule;
import org.jetbrains.mps.openapi.persistence.ModelRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class StubSolutionIdea extends StubSolution {
  private ModelAccess myModelAccess;

  private final RootSetChangedListener myRootSetChangedListener = new RootSetChangedListener() {
    @Override
    public void rootSetChanged(RootProvider wrapper) {
      myModelAccess.runWriteAction(new Runnable() {
        @Override
        public void run() {
          SolutionDescriptor moduleDescriptor = getModuleDescriptor();
          moduleDescriptor.getModelRootDescriptors().clear();
          addModelRoots(moduleDescriptor, getRootProvider().getFiles(OrderRootType.CLASSES));
          setModuleDescriptor(moduleDescriptor);
        }
      });
    }
  };

  protected StubSolutionIdea(SolutionDescriptor descriptor, ModelAccess modelAccess) {
    super(descriptor, null);
    myModelAccess = modelAccess;
  }

  @Nullable
  public static Solution newInstance(Library library, MPSModuleOwner moduleOwner, SRepositoryExt repository) throws StubModuleNameTakenException {
    String namespace = library.getName();
    if (namespace != null && new ModuleRepositoryFacade(repository).getModuleByName(namespace) != null) {
      throw new StubModuleNameTakenException(library.getName(), namespace);
    }
    SolutionDescriptor descriptor = createDescriptor(namespace, library.getFiles(OrderRootType.CLASSES));
    return register(repository, moduleOwner, new LibraryStubSolution(descriptor, library, repository.getModelAccess()));
  }

  public static Solution newInstance(Sdk sdk, Sdk baseJdk, MPSModuleOwner moduleOwner, SRepositoryExt repository) {
    SolutionDescriptor descriptor = createDescriptor(sdk.getName(), ((SdkModificator) sdk).getRoots(OrderRootType.CLASSES));
    return register(repository, moduleOwner, new SdkStubSolution(descriptor, sdk, baseJdk, repository.getModelAccess()));
  }

  public static Solution newInstanceForJdk(Sdk sdk, MPSModuleOwner moduleOwner, SRepositoryExt repository) {
    SolutionDescriptor descriptor = createDescriptor("JDK", ((SdkModificator) sdk).getRoots(OrderRootType.CLASSES));

    // giving the SDK the hard-coded module id
    ModuleId jdkId = ModuleId.regular(UUID.fromString("6354ebe7-c22a-4a0f-ac54-50b52ab9b065"));
    SModule jdkModule = repository.getModule(jdkId);
    if (jdkModule != null) {
      Set<MPSModuleOwner> owners = new HashSet<MPSModuleOwner>(new ModuleRepositoryFacade(repository).getModuleOwners(jdkModule));
      for (MPSModuleOwner owner : owners) {
        // FIXME unregister leads to warnings in ModuleUpdater.updateAllEdges()
        // we register it back in the same write action but listener has the time to see the bad state:
        // JDK module is missing and a lot depends on it
        repository.unregisterModule(jdkModule, owner);
      }
    }

    descriptor.setId(jdkId);

    return register(repository, moduleOwner, new SdkStubSolution(descriptor, sdk, null, repository.getModelAccess()));
  }

  public static Solution newInstanceForRoots(Sdk sdk, Sdk baseJdk, VirtualFile[] roots, MPSModuleOwner moduleOwner, SRepositoryExt repository) {
    SolutionDescriptor descriptor = createDescriptor(sdk.getName(), roots);
    return register(repository, moduleOwner, new SdkStubSolution(descriptor, sdk, baseJdk, repository.getModelAccess()));
  }


  @Nullable
  public static Library findLibrary(StubSolutionIdea solutionIdea) {
    if (solutionIdea instanceof LibraryStubSolution) {
      return ((LibraryStubSolution) solutionIdea).getLibrary();
    }
    // sdk?
    return null;
  }

  private static SolutionDescriptor createDescriptor(String name, VirtualFile[] roots) {
    SolutionDescriptor sd = new SolutionDescriptor();
    sd.setNamespace(name);
    sd.setId(ModuleId.foreign(name));
    sd.setCompileInMPS(false);
    sd.getModuleFacetDescriptors().add(new ModuleFacetDescriptor(IdeaPluginModuleFacet.FACET_TYPE, new MementoImpl()));
    addModelRoots(sd, roots);
    return sd;
  }

  protected void attachRootsListener() {
    getRootProvider().addRootSetChangedListener(myRootSetChangedListener);
  }

  protected abstract RootProvider getRootProvider();

  public static List<ModelRoot> getModelRoots(VirtualFile[] roots) {
    List<ModelRoot> result = new ArrayList<ModelRoot>();

    for (VirtualFile f : roots) {
      String localPath = getLocalPath(f);
      JavaClassStubsModelRoot modelRoot = new JavaClassStubsModelRoot();
      modelRoot.setContentRoot(localPath);
      modelRoot.addFile(FileBasedModelRoot.SOURCE_ROOTS, localPath);
      result.add(modelRoot);
    }

    return result;
  }

  public static void addModelRoots(SolutionDescriptor solutionDescriptor, VirtualFile[] roots) {
    Set<String> seenPaths = new LinkedHashSet<String>();
    for (ModelRootDescriptor d : solutionDescriptor.getModelRootDescriptors()) {
      if (!PersistenceRegistry.JAVA_CLASSES_ROOT.equals(d.getType())) continue;

      seenPaths.add(d.getMemento().get("path"));
    }
    for (VirtualFile f : roots) {
      String localPath = getLocalPath(f);
      if (!seenPaths.add(localPath)) continue;
      solutionDescriptor.getModelRootDescriptors().add(ModelRootDescriptor.getJavaStubsModelRoot(FileSystem.getInstance().getFile(localPath)));
    }
  }

  private static String getLocalPath(VirtualFile f) {
    String path = f.getPath();
    int index = path.indexOf("!");
    if (index < 0) return path;
    return path.substring(0, index);
  }

  @Override
  public void dispose() {
    getRootProvider().removeRootSetChangedListener(myRootSetChangedListener);
    super.dispose();
  }

  @Override
  public Iterable<SDependency> getDeclaredDependencies() {
    Set<SDependency> deps = new HashSet<SDependency>();

    // explicitly add jdk
    // todo: remove when sdk are loaded correctly
//      Solution jdkSolutionReference = getJdkSolution();
//      if (jdkSolutionReference != null) {
//        modules.add(jdkSolutionReference);
//      }

    for (SModule module : ModuleRepositoryFacade.getInstance().getAllModules(StubSolutionIdea.class)) {
      deps.add(new SDependencyImpl(module, SDependencyScope.DEFAULT, false));
    }
    return deps;
  }

  @Nullable
  public static Solution getJdkSolution() {
    return ModuleRepositoryFacade.getInstance().getModule(BootstrapLanguages.jdkRef(), Solution.class);
  }

  private static class LibraryStubSolution extends StubSolutionIdea {
    @NotNull
    private final Library myLibrary;

    protected LibraryStubSolution(SolutionDescriptor descriptor, @NotNull Library library, ModelAccess modelAccess) {
      super(descriptor, modelAccess);
      myLibrary = library;
      attachRootsListener();
      // todo handle rename; no idea how
    }

    @Override
    protected RootProvider getRootProvider() {
      return myLibrary.getRootProvider();
    }

    @NotNull
    public Library getLibrary() {
      return myLibrary;
    }
  }

  private static class SdkStubSolution extends StubSolutionIdea {
    @NotNull
    private final Sdk mySdk;
    private final Sdk myBaseJdk;

    protected SdkStubSolution(SolutionDescriptor descriptor, @NotNull Sdk sdk, Sdk baseJdk, ModelAccess modelAccess) {
      super(descriptor, modelAccess);
      mySdk = sdk;
      myBaseJdk = baseJdk;
      setUpdateBootstrapSolutions(false);
      attachRootsListener();
    }

    @Override
    protected RootProvider getRootProvider() {
      return mySdk.getRootProvider();
    }

    @Override
    public Iterable<SDependency> getDeclaredDependencies() {
      if (myBaseJdk == null) return Collections.emptySet();

      Solution baseJdkSolution = ApplicationManager.getApplication().getComponent(JdkStubSolutionManager.class).getSdkSolution(myBaseJdk, getRepository());
      return Collections.<SDependency>singleton(new SDependencyImpl(baseJdkSolution, SDependencyScope.DEFAULT, true));
    }
  }

  @Override
  public String toString() {
    return getModuleName() + " [idea stub solution]";
  }
}
