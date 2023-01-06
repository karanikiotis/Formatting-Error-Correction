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
package com.oracle.truffle.r.runtime.data;

import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;

/**
 * An {@link RSequence} only records the "length", "start" and "stride" (the latter two fields in
 * the subclasses {@link RIntSequence} and {@link RDoubleSequence}. The "stride" value is positive
 * to indicate an ascending sequence and negative for a descending sequence. I.e., the "end" is
 * computed and not stored.
 */
public abstract class RSequence implements RAbstractVector {

    private final int length;

    protected RSequence(int length) {
        this.length = length;
    }

    @Override
    public final int getLength() {
        return length;
    }

    @Override
    public RAbstractContainer resize(int size) {
        return materialize().resize(size);
    }

    public abstract Object getStartObject();

    public abstract Object getStrideObject();

    @Override
    public final boolean isComplete() {
        return true;
    }

    @Override
    public boolean checkCompleteness() {
        return true;
    }

    @Override
    public void setComplete(boolean complete) {
        // sequences are always complete
    }

    @Override
    public final boolean hasDimensions() {
        return false;
    }

    @Override
    public final int[] getDimensions() {
        return null;
    }

    @Override
    public void setDimensions(int[] newDimensions) {
        // should only be used on materialized sequence
        throw RInternalError.shouldNotReachHere();
    }

    public final RVector<?> createVector() {
        RVector<?> result = internalCreateVector();
        MemoryCopyTracer.reportCopying(this, result);
        return result;
    }

    protected abstract RVector<?> internalCreateVector();

    @Override
    public final RAbstractVector copy() {
        return createVector();
    }

    @Override
    public final RAbstractVector copyDropAttributes() {
        return createVector();
    }

    @Override
    public final RAbstractVector copyWithNewDimensions(int[] newDimensions) {
        return createVector().copyWithNewDimensions(newDimensions);
    }

    @Override
    public final RStringVector getNames() {
        return null;
    }

    @Override
    public void setNames(RStringVector newNames) {
        // should only be used on materialized sequence
        throw RInternalError.shouldNotReachHere();
    }

    @Override
    public final RList getDimNames() {
        return null;
    }

    @Override
    public void setDimNames(RList newDimNames) {
        // should only be used on materialized sequence
        throw RInternalError.shouldNotReachHere();
    }

    @Override
    public final Object getRowNames() {
        return RNull.instance;
    }

    @Override
    public void setRowNames(RAbstractVector rowNames) {
        // should only be used on materialized sequence
        throw RInternalError.shouldNotReachHere();
    }

    @Override
    public final DynamicObject initAttributes() {
        throw RInternalError.shouldNotReachHere();
    }

    @Override
    public final void initAttributes(DynamicObject newAttributes) {
        throw RInternalError.shouldNotReachHere();
    }

    @Override
    public final DynamicObject getAttributes() {
        return null;
    }

    @Override
    public final boolean isMatrix() {
        return false;
    }

    @Override
    public final boolean isArray() {
        return false;
    }

    @Override
    public final boolean isObject() {
        return false;
    }

    @Override
    public final RTypedValue getNonShared() {
        return materialize().getNonShared();
    }

    @Override
    public int getTypedValueInfo() {
        return 0;
    }

    @Override
    public void setTypedValueInfo(int value) {
        throw RInternalError.shouldNotReachHere();
    }

    @Override
    public boolean isS4() {
        return false;
    }
}
