/*
 * Copyright 2003-2011 JetBrains s.r.o.
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
package jetbrains.mps.ide.findusages.view.optionseditor.components;

import com.intellij.icons.AllIcons.Nodes;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import jetbrains.mps.ide.findusages.FindersManager;
import jetbrains.mps.ide.findusages.findalgorithm.finders.IInterfacedFinder;
import jetbrains.mps.ide.findusages.view.optionseditor.options.FindersOptions;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.mps.openapi.model.SNode;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuKeyEvent;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class FindersEditor extends BaseEditor<FindersOptions> {
  public FindersEditor(FindersOptions defaultOptions, final SNode node) {
    this(defaultOptions, node, null);
  }

  public FindersEditor(FindersOptions defaultOptions, final SNode node, @Nullable final Consumer<IInterfacedFinder> navigation) {
    super(defaultOptions);

    myPanel = new JPanel();
    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

    myPanel.setBorder(IdeBorderFactory.createTitledBorder("Finders", false));

    Set<IInterfacedFinder> availableFinders = FindersManager.getInstance().getAvailableFinders(node);

    List<IInterfacedFinder> sortedFinders = new ArrayList<>(availableFinders);
    Collections.sort(sortedFinders, (o1, o2) -> o1.getDescription().compareToIgnoreCase(o2.getDescription()));

    for (final IInterfacedFinder finder : sortedFinders) {
      boolean isEnabled = myOptions.getFindersClassNames().contains(finder.getClass().getName());

      final JBCheckBox finderCheckBox = new JBCheckBox(finder.getDescription(), isEnabled);
      finderCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

      finderCheckBox.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
          String finderClassName = finder.getClass().getName();
          if (((JCheckBox) e.getSource()).isSelected()) {
            if (!myOptions.getFindersClassNames().contains(finderClassName)) {
              myOptions.getFindersClassNames().add(finderClassName);
              findersListChangedByUser();
            }
          } else {
            myOptions.getFindersClassNames().remove(finderClassName);
            findersListChangedByUser();
          }
        }
      });

      if (!finder.getLongDescription().equals("")) {
        StringBuilder htmlTooltipText = new StringBuilder();
        htmlTooltipText.append("<html>").append(finder.getLongDescription().replaceAll("\n", "<br>")).append(")</html>");
        finderCheckBox.setToolTipText(htmlTooltipText.toString());
      }

      JToolBar finderHolder = new JToolBar(JToolBar.HORIZONTAL);
      finderHolder.add(finderCheckBox);

      if (navigation != null && finder.canNavigate()) {
        finderCheckBox.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            if ((e.getKeyCode() == MenuKeyEvent.VK_B) && (e.getID() == MenuKeyEvent.KEY_PRESSED) && (e.isControlDown())) {
              navigation.accept(finder);
              e.consume();
            }
          }
        });

        JBLabel goToFinderLabel = new JBLabel(Nodes.Symlink, JLabel.CENTER);
        goToFinderLabel.setBorder(BorderFactory.createEmptyBorder());
        goToFinderLabel.setFocusable(false);
        goToFinderLabel.setToolTipText("Go to finder declaration");
        goToFinderLabel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            navigation.accept(finder);
          }
        });
        goToFinderLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        finderHolder.add(goToFinderLabel);
      }

      finderHolder.setBorder(new EmptyBorder(0, 0, 0, 0));
      finderHolder.setFloatable(false);
      finderHolder.setAlignmentX(JToolBar.LEFT_ALIGNMENT);
      finderHolder.setBackground(myPanel.getBackground());

      myPanel.add(finderHolder);
    }
  }

  protected void findersListChangedByUser() {

  }
}
