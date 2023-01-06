/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hyracks.storage.am.lsm.common.impls;

import java.util.Objects;

import org.apache.hyracks.api.exceptions.HyracksDataException;
import org.apache.hyracks.api.io.FileReference;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMComponent;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMIOOperationCallback;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMIndexAccessor;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMMemoryComponent;

public class FlushOperation extends AbstractIoOperation implements Comparable<FlushOperation> {

    protected final ILSMMemoryComponent flushingComponent;

    public FlushOperation(ILSMIndexAccessor accessor, ILSMMemoryComponent flushingComponent, FileReference target,
            ILSMIOOperationCallback callback, String indexIdentifier) {
        super(accessor, target, callback, indexIdentifier);
        this.flushingComponent = flushingComponent;
    }

    @Override
    public Boolean call() throws HyracksDataException {
        accessor.flush(this);
        return true;
    }

    @Override
    public ILSMIOOperationCallback getCallback() {
        return callback;
    }

    @Override
    public FileReference getTarget() {
        return target;
    }

    @Override
    public ILSMIndexAccessor getAccessor() {
        return accessor;
    }

    public ILSMComponent getFlushingComponent() {
        return flushingComponent;
    }

    @Override
    public String getIndexIdentifier() {
        return indexIdentifier;
    }

    @Override
    public LSMIOOpertionType getIOOpertionType() {
        return LSMIOOpertionType.FLUSH;
    }

    @Override
    public int compareTo(FlushOperation o) {
        return target.getFile().getName().compareTo(o.getTarget().getFile().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FlushOperation)) {
            return false;
        }
        return Objects.equals(target.getFile().getName(), ((FlushOperation) o).target.getFile().getName());
    }

    @Override
    public int hashCode() {
        return target.getFile().getName().hashCode();
    }
}
