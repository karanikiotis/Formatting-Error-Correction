/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.gs.fw.common.mithra.remote;

import com.gs.fw.common.mithra.finder.Operation;

import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;



public class RemoteDeleteUsingOperationResult extends MithraRemoteResult
{

    private transient Operation op;

    public RemoteDeleteUsingOperationResult()
    {
        // for externalizable
    }

    public RemoteDeleteUsingOperationResult(Operation op)
    {
        this.op = op;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        this.writeRemoteTransactionId(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.readRemoteTransactionId(in);
    }

    public void run()
    {
        op.getResultObjectPortal().getMithraObjectPersister().deleteUsingOperation(op);
    }
}
