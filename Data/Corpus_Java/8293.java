/*
 * Copyright (c) 2013, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.r.nodes.builtin.RBuiltinFactory;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.nodes.function.FormalArguments;
import com.oracle.truffle.r.nodes.function.FunctionDefinitionNode;
import com.oracle.truffle.r.runtime.HasSignature;
import com.oracle.truffle.r.runtime.RArguments;
import com.oracle.truffle.r.runtime.builtins.FastPathFactory;
import com.oracle.truffle.r.runtime.context.TruffleRLanguage;

/**
 * The base class for R code that can be executed, namely {@link FunctionDefinitionNode} and
 * {@link RBuiltinNode}.
 */
public abstract class RRootNode extends RootNode implements HasSignature {

    private final ConditionProfile irregularFrameProfile = ConditionProfile.createBinaryProfile();

    private FastPathFactory fastPath;

    protected RRootNode(TruffleRLanguage language, FrameDescriptor frameDescriptor, FastPathFactory fastPath) {
        super(language, frameDescriptor);
        this.fastPath = fastPath;
    }

    @Override
    public abstract SourceSection getSourceSection();

    @Override
    public abstract RootCallTarget duplicateWithNewFrameDescriptor();

    protected final void verifyEnclosingAssumptions(VirtualFrame vf) {
        RArguments.setIsIrregular(vf, irregularFrameProfile.profile(RArguments.getIsIrregular(vf)));
    }

    public final FastPathFactory getFastPath() {
        return fastPath;
    }

    public final void setFastPath(FastPathFactory fastPath) {
        this.fastPath = fastPath;
    }

    public abstract FormalArguments getFormalArguments();

    public abstract boolean needsSplitting();

    public abstract boolean containsDispatch();

    public abstract void setContainsDispatch(boolean containsDispatch);

    public abstract RBuiltinFactory getBuiltin();

    @Override
    public boolean isCloningAllowed() {
        return true;
    }

    @Override
    public final String toString() {
        return getName();
    }
}
