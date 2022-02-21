/*
 * Copyright 2014 Google Inc. All rights reserved.
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
package org.inferred.freebuilder.processor.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

/**
 * Fake implementation of {@link DeclaredType} for unit tests.
 */
public abstract class ClassTypeImpl implements DeclaredType {

  private final Element enclosingElement;
  private final TypeMirror enclosingType;
  private final String simpleName;

  public static ClassTypeImpl newTopLevelClass(String qualifiedName) {
    String pkg = qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));
    String simpleName = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    PackageElement enclosingElement = PackageElementImpl.create(pkg);
    return Partial.of(ClassTypeImpl.class, enclosingElement, NoTypes.NONE, simpleName);
  }

  public static ClassTypeImpl newNestedClass(TypeElement enclosingType, String simpleName) {
    return Partial.of(ClassTypeImpl.class, enclosingType, NoTypes.NONE, simpleName);
  }

  public static ClassTypeImpl newInnerClass(DeclaredType enclosingType, String simpleName) {
    return Partial.of(ClassTypeImpl.class, enclosingType.asElement(), enclosingType, simpleName);
  }

  ClassTypeImpl(Element enclosingElement, TypeMirror enclosingType, String simpleName) {
    this.enclosingElement = enclosingElement;
    this.enclosingType = enclosingType;
    this.simpleName = simpleName;
  }

  @Override
  public TypeKind getKind() {
    return TypeKind.DECLARED;
  }

  @Override
  public <R, P> R accept(TypeVisitor<R, P> v, P p) {
    return v.visitDeclared(this, p);
  }

  @Override
  public ClassElementImpl asElement() {
    return Partial.of(ClassElementImpl.class, this);
  }

  @Override
  public TypeMirror getEnclosingType() {
    return enclosingType;
  }

  @Override
  public List<? extends TypeMirror> getTypeArguments() {
    return ImmutableList.of();
  }

  @Override
  public String toString() {
    final String prefix;
    if (enclosingElement.getKind() == ElementKind.PACKAGE) {
      prefix = ((PackageElement) enclosingElement).getQualifiedName() + ".";
    } else {
      prefix = ((TypeElement) enclosingElement).getQualifiedName() + ".";
    }
    return prefix + simpleName;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof ClassTypeImpl) && toString().equals(o.toString());
  }

  /**
   * Fake implementation of {@link TypeElement} for unit tests.
   */
  public abstract class ClassElementImpl implements TypeElement {

    @Override
    public ClassTypeImpl asType() {
      return ClassTypeImpl.this;
    }

    @Override
    public ElementKind getKind() {
      return ElementKind.CLASS;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
      return v.visitType(this, p);
    }

    @Override
    public NestingKind getNestingKind() {
      return (enclosingElement.getKind() == ElementKind.PACKAGE)
          ? NestingKind.TOP_LEVEL : NestingKind.MEMBER;
    }

    @Override
    public Name getQualifiedName() {
      return new NameImpl(ClassTypeImpl.this.toString());
    }

    @Override
    public Name getSimpleName() {
      return new NameImpl(simpleName);
    }

    @Override
    public TypeMirror getSuperclass() {
      return NoTypes.NONE;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
      return ImmutableList.of();
    }

    @Override
    public Element getEnclosingElement() {
      return enclosingElement;
    }
  }
}
