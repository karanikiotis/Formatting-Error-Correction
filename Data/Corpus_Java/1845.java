/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.dsl.test.interop;

import com.oracle.truffle.api.dsl.test.ExpectError;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@SuppressWarnings("unused")
@MessageResolution(receiverType = VisibilityTestObject.class)
class VisibilityTestObjectMR3 {

    @Resolve(message = "READ")
    abstract static class VisibilityTestReadNode3 extends Node {

        protected Object access(VirtualFrame frame, VisibilityTestObject receiver, Object name) {
            return 0;
        }
    }

    @CanResolve
    @ExpectError({"Class must be public or package protected"})
    protected abstract static class VisibilityTestLanguageCheck3 extends Node {

        protected boolean test(VirtualFrame frame, TruffleObject receiver) {
            return receiver instanceof VisibilityTestObject;
        }
    }

}
