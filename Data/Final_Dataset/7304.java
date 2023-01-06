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

import com.gs.fw.common.mithra.MithraDataObject;
import com.gs.fw.common.mithra.MithraDatedObject;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;



public class RemoteRefreshDatedObjectResult extends MithraRemoteResult
{

    private transient MithraDatedObject mithraDatedObject;
    private transient boolean lock;
    private MithraDataObject refreshedData;

    public RemoteRefreshDatedObjectResult()
    {
        // for externalizable
    }

    public RemoteRefreshDatedObjectResult(MithraDatedObject mithraDatedObject, boolean lock)
    {
        this.mithraDatedObject = mithraDatedObject;
        this.lock = lock;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        this.writeRemoteTransactionId(out);
        out.writeUTF(MithraSerialUtil.getDataClassNameToSerialize(this.refreshedData));
        this.refreshedData.zSerializeFullData(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.readRemoteTransactionId(in);
        Class dataClass = MithraSerialUtil.getDataClassToInstantiate(in.readUTF());
        this.refreshedData = MithraSerialUtil.instantiateData(dataClass);
        this.refreshedData.zDeserializeFullData(in);
    }

    public void run()
    {
        this.refreshedData = mithraDatedObject.zRefreshWithLock(lock);
    }

    public MithraDataObject getResultData()
    {
        return refreshedData;
    }
}
