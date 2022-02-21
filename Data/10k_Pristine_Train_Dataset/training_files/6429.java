/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.weld.tests.contexts.passivating.serialization.interception;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

/**
 *
 */
@SessionScoped
@Intercepted
public class FooImpl implements Foo, Serializable {

    public void methodWithObjectParameter(String str) {
    }

    public void methodWithPrimitiveParameters(byte b, short s, int i, long l, float f, double d, boolean bool, char ch) {
    }

    public void methodWithObjectArrayParameter(String[] strArray) {
    }

    public void methodWithPrimitiveArrayParameter(int[] intArray) {
    }

    public void methodWithInnerClassParameter(InnerClass innerClass) {
    }

    public static class InnerClass {

    }

}
