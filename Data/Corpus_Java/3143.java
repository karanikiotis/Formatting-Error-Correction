package jetbrains.mps.idea.sourceStubs;

import com.intellij.openapi.module.Module;
import jetbrains.mps.idea.core.project.ModelRootContributor;
import jetbrains.mps.idea.java.psiStubs.PsiJavaStubModelRoot;
import org.jetbrains.mps.openapi.persistence.ModelRoot;

import java.util.ArrayList;
import java.util.List;

/**
 * danilla 12/7/12
 */

public class JavaSourceModelRootContributor implements ModelRootContributor {

  @Override
  public Iterable<ModelRoot> getModelRoots(Module module) {
    List<ModelRoot> modelRoots = new ArrayList<ModelRoot>(1);
    modelRoots.add(new PsiJavaStubModelRoot(module));
    return modelRoots;
  }

}
