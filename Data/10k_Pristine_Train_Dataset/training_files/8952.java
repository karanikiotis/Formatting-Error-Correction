/*
 * Copyright 2003-2014 JetBrains s.r.o.
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
package jetbrains.mps.nodeEditor.cellMenu;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.containers.FList;
import com.intellij.util.ui.UIUtil;
import jetbrains.mps.ide.icons.IconManager;
import jetbrains.mps.ide.icons.IdeIcons;
import jetbrains.mps.nodeEditor.EditorSettings;
import jetbrains.mps.openapi.editor.cells.SubstituteAction;
import jetbrains.mps.smodel.SNodeUtil;
import jetbrains.mps.smodel.adapter.MetaAdapterByDeclaration;
import jetbrains.mps.smodel.presentation.NodePresentationUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.mps.openapi.model.SNode;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

class NodeItemCellRenderer extends JPanel implements ListCellRenderer<SubstituteAction> {
  private static final Logger LOG = LogManager.getLogger(NodeItemCellRenderer.class);
  private static final String EXCEPTION_WAS_THROWN_TEXT = "!Exception was thrown!";

  private SimpleColoredComponent myLeft = new SimpleColoredComponent();
  private SimpleColoredComponent myRight = new SimpleColoredComponent();
  private static final int HORIZONTAL_GAP = 10;
  private final Color HIGHLIGHT_COLOR = UIUtil.isUnderDarcula() ? new Color(217, 149, 219) : new Color(189, 55, 186);
  private final Color SELECTION_HIGHLIGHT_COLOR = UIUtil.isUnderDarcula() ? HIGHLIGHT_COLOR : new Color(250, 239, 215);
  private int myStyle = Font.PLAIN;
  private Map<SNode, Icon> myNodeIconMap = new HashMap<SNode, Icon>();
  private Map<SNode, Icon> myConceptIconMap = new HashMap<SNode, Icon>();
  private final NodeSubstituteChooser mySubstituteChooser;

  private int myMaxWidth = 0;
  private int myMaxHeight = 0;

  NodeItemCellRenderer(@NotNull NodeSubstituteChooser substituteChooser) {
    mySubstituteChooser = substituteChooser;
    setLayout(new BorderLayout(HORIZONTAL_GAP / 2, 0));
    myLeft.setFont(EditorSettings.getInstance().getDefaultEditorFont());
    myRight.setFont(EditorSettings.getInstance().getDefaultEditorFont());
    add(myLeft, BorderLayout.WEST);
    add(myRight, BorderLayout.EAST);
  }

  @Override
  public Component getListCellRendererComponent(final JList list, final SubstituteAction action, int index, final boolean isSelected, boolean cellHasFocus) {
    mySubstituteChooser.getEditorComponent()
                       .getEditorContext()
                       .getRepository()
                       .getModelAccess()
                       .runReadAction(() -> setupThis(list, action, isSelected, false));
    return this;
  }


  Dimension getDimension(SubstituteAction action, JList list) {
    setupThis(list, action, false, true);
    return getPreferredSize().getSize();
  }

  private void setupThis(JList list, SubstituteAction action, boolean isSelected, boolean isPrecalculating) {
    myLeft.clear();
    myRight.clear();
    String pattern = mySubstituteChooser.getPatternEditor().getPattern();
    try {
      Icon icon = getIcon(action, pattern);
      myLeft.setIcon(icon);
    } catch (Throwable t) {
      LOG.error(null, t);
    }

    try {
      int style = getStyle(action);
      if (myStyle != style) {
        Font font = getFont(action);
        myLeft.setFont(font);
        myRight.setFont(font);
        myStyle = style;
      }

    } catch (Throwable t) {
      LOG.error(null, t);
    }

    try {
      String visibleMatchingText = action.getVisibleMatchingText(pattern);
      if (visibleMatchingText != null) {
        appendText(pattern, myLeft, isSelected, visibleMatchingText);
      }
    } catch (Throwable t) {
      myLeft.append(EXCEPTION_WAS_THROWN_TEXT);
      LOG.error(null, t);
    }

    try {
      String descriptionText = action.getDescriptionText(pattern);
      if (descriptionText != null) {
        myRight.append(descriptionText);
      }
    } catch (Throwable t) {
      myRight.append(EXCEPTION_WAS_THROWN_TEXT);
      LOG.error(null, t);
    }

    if (isSelected) {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
      myLeft.setForeground(list.getSelectionForeground());
      myRight.setForeground(list.getSelectionForeground());
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
      myLeft.setForeground(list.getForeground());
      myRight.setForeground(list.getForeground());
    }

    if (!isPrecalculating) {
      validate();
      Dimension preferredSize = getPreferredSize();
      if (myMaxHeight < preferredSize.height || myMaxWidth < preferredSize.width) {
        myMaxWidth = Math.max(myMaxWidth, preferredSize.width);
        myMaxHeight = Math.max(myMaxHeight, preferredSize.height);
        mySubstituteChooser.getUi().updateListSize(myMaxWidth, myMaxHeight);
      }
    }
  }

  private void appendText(String pattern, SimpleColoredComponent component, boolean isSelected, String text) {
    Color foreground = isSelected ? NodeSubstituteChooserUi.SELECTED_FOREGROUND_COLOR : NodeSubstituteChooserUi.FOREGROUND_COLOR;
    final SimpleTextAttributes base = new SimpleTextAttributes(myStyle, foreground);

    Iterable<TextRange> ranges = getMatchingFragments(pattern, text);
    if (ranges != null) {
      SimpleTextAttributes highlighted =
          new SimpleTextAttributes(myStyle, isSelected ? SELECTION_HIGHLIGHT_COLOR : HIGHLIGHT_COLOR);
      SpeedSearchUtil.appendColoredFragments(component, text, ranges, base, highlighted);
    } else {
      component.append(text, base);
    }
  }

  private static FList<TextRange> getMatchingFragments(String pattern, String text) {
    return getMatcher(pattern).matchingFragments(text);
  }

  private static MinusculeMatcher getMatcher(String pattern) {
    return NameUtil.buildMatcher("*" + pattern).build();
  }

  private int getStyle(SubstituteAction action) {
    int style;
    final Object parameterObject = action.getParameterObject();
    if (parameterObject instanceof SNode) {
      style = NodePresentationUtil.getFontStyle(action.getSourceNode(), (SNode) parameterObject);
    } else {
      style = Font.PLAIN;
    }
    return style;
  }

  private Font getFont(SubstituteAction action) {
    Font font = EditorSettings.getInstance().getDefaultEditorFont();
    try {
      int style = getStyle(action);
      font = font.deriveFont(style);
    } catch (Throwable t) {
      LOG.error(null, t);
    }
    return font;
  }

  private Icon getIcon(SubstituteAction action, String pattern) {
    Icon icon = null;
    if (action instanceof CompletionActionItemAsSubstituteAction) {
      icon = IconManager.getIconForResource(((CompletionActionItemAsSubstituteAction) action).getIcon(pattern));
    }
    if (icon != null) {
      return icon;
    }
    SNode iconNode = action.getIconNode(pattern);
    if (iconNode != null) {
      boolean isConcept = SNodeUtil.isInstanceOfConceptDeclaration(iconNode) && !(action.isReferentPresentation());
      if (isConcept) {
        icon = myConceptIconMap.get(iconNode);
      } else {
        icon = myNodeIconMap.get(iconNode);
      }
      if (icon == null) {
        if (isConcept) {
          icon = IconManager.getIcon(MetaAdapterByDeclaration.getConcept(iconNode));
          if (icon == null) {
            icon = IdeIcons.DEFAULT_NODE_ICON;
          }
          myConceptIconMap.put(iconNode, icon);
        } else {
          icon = IconManager.getIconFor(iconNode);
          myNodeIconMap.put(iconNode, icon);
        }
      }
    }
    if (icon == null) {
      icon = IdeIcons.DEFAULT_ICON;
    }
    return icon;
  }
}
