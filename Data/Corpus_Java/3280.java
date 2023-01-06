/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.tests.decorators.ejb;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.weld.test.util.ActionSequence;

@Decorator
public class Decorator1 implements Vehicle, Serializable {

    private static final long serialVersionUID = -288060710833519754L;

    @Inject
    @Delegate
    @Any
    private Vehicle delegate;

    @Override
    public void start() {
        ActionSequence.addAction(Decorator1.class.getSimpleName());
        delegate.start();
        ActionSequence.addAction(Decorator1.class.getSimpleName());
    }
}
