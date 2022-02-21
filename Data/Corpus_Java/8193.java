/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.javascript.tree.impl.expression.jsx;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxToken;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.expression.jsx.JsxAttributeValueTree;
import org.sonar.plugins.javascript.api.tree.expression.jsx.JsxIdentifierTree;
import org.sonar.plugins.javascript.api.tree.expression.jsx.JsxStandardAttributeTree;
import org.sonar.plugins.javascript.api.visitors.DoubleDispatchVisitor;

public class JsxStandardAttributeTreeImpl extends JavaScriptTree implements JsxStandardAttributeTree {

  private final JsxIdentifierTree name;
  private final InternalSyntaxToken equalToken;
  private final JsxAttributeValueTree value;

  public JsxStandardAttributeTreeImpl(JsxIdentifierTree name, InternalSyntaxToken equalToken, JsxAttributeValueTree value) {
    this.name = name;
    this.equalToken = equalToken;
    this.value = value;
  }

  @Override
  public Kind getKind() {
    return Kind.JSX_STANDARD_ATTRIBUTE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(name, equalToken, value);
  }

  @Override
  public JsxIdentifierTree name() {
    return name;
  }

  @Override
  public InternalSyntaxToken equalToken() {
    return equalToken;
  }

  @Override
  public JsxAttributeValueTree value() {
    return value;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitJsxStandardAttribute(this);
  }
}
