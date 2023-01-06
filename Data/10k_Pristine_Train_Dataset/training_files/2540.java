/*
 * Copyright 2003-2016 JetBrains s.r.o.
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
package jetbrains.mps.ide.ui.tree.module;

import jetbrains.mps.icons.MPSIcons.Nodes.Models;
import jetbrains.mps.ide.ui.tree.ErrorState;
import jetbrains.mps.ide.ui.tree.TextTreeNode;
import jetbrains.mps.project.dependency.VisibilityUtil;
import jetbrains.mps.smodel.Language;
import org.jetbrains.mps.openapi.model.SModel;
import org.jetbrains.mps.openapi.model.SModelReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessoriesModelTreeNode extends TextTreeNode {
  private ProjectLanguageTreeNode myProjectLanguageTreeNode;

  public AccessoriesModelTreeNode(ProjectLanguageTreeNode projectLanguageTreeNode) {
    super("accessories");
    myProjectLanguageTreeNode = projectLanguageTreeNode;
    setIcon(Models.AccessoryModel);
  }

  public List<String> validate() {
    Language lang = myProjectLanguageTreeNode.getModule();
    if (lang.getRepository() == null) {
      return Collections.emptyList();
    }
    List<String> errors = new ArrayList<>();
    //this check is wrong in common as we don't know what the user wants to do with the acc model in build.
    //but I'll not delete it until accessories removal just to have some warning on project consistency
    for (SModelReference accessory : lang.getModuleDescriptor().getAccessoryModels()) {
      SModel accModel = accessory.resolve(lang.getRepository());
      if (accModel == null) {
        continue;
      }

      if (!VisibilityUtil.isVisible(lang, accModel)) {
        errors.add("Can't find accessory " + accessory.getName());
      }
    }
    return errors;
  }

  @Override
  protected void doUpdatePresentation() {
    super.doUpdatePresentation();
    setErrorState(validate().isEmpty() ? ErrorState.NONE : ErrorState.ERROR);
  }
}
