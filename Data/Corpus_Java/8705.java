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
package jetbrains.mps.workbench.structureview.nodes;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import jetbrains.mps.plugins.relations.RelationDescriptor;
import jetbrains.mps.workbench.choose.NodePointerNavigationItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.mps.openapi.language.SConcept;
import org.jetbrains.mps.openapi.model.SNode;
import org.jetbrains.mps.openapi.model.SNodeReference;

public class AspectTreeElement implements StructureViewTreeElement, Comparable<AspectTreeElement> {
  private final MainNodeTreeElement myParent;
  private final boolean myIsBijectional;
  private final RelationDescriptor myAspectDescriptor;
  private final NodePointerNavigationItem myPresentation;
  private final SConcept myNodeConcept;

  // invoked with model read
  /*package*/ AspectTreeElement(MainNodeTreeElement parent, SNode node, RelationDescriptor aspectDescriptor, boolean bijectional) {
    myParent = parent;
    myAspectDescriptor = aspectDescriptor;
    myIsBijectional = bijectional;
    myPresentation = new Presentation(node);
    myNodeConcept = node.getConcept();
  }

  public RelationDescriptor getAspectDescriptor() {
    return myAspectDescriptor;
  }

  public boolean isBijectional() {
    return myIsBijectional;
  }

  @Override
  public TreeElement[] getChildren() {
    return StructureViewTreeElement.EMPTY_ARRAY;
  }

  @Override
  public ItemPresentation getPresentation() {
    return myPresentation;
  }

  @Override
  public SNodeReference getValue() {
    return myPresentation.getNodePointer();
  }

  @Override
  public boolean canNavigate() {
    return true;
  }

  @Override
  public boolean canNavigateToSource() {
    return true;
  }

  @Override
  public void navigate(boolean b) {
    myParent.navigate(getValue());
  }

  @Override
  public int compareTo(@NotNull AspectTreeElement o) {
    RelationDescriptor d1 = myAspectDescriptor;
    RelationDescriptor d2 = o.myAspectDescriptor;

    int r1 = d1.compareTo(d2);
    int r2 = d2.compareTo(d1);

    if ((r1 == 0) ^ (r2 == 0)) {
      return r1 - r2;
    }

    assert r1 * r2 <= 0 : "can't determine order";

    if (r1 != 0) {
      return r1;
    }

    return myNodeConcept.getName().compareTo(o.myNodeConcept.getName());
  }
}
