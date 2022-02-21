/*
 * Copyright 2010 Outerthought bvba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lilyproject.hbaseindex;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.common.base.Preconditions;
import com.gotometrics.orderly.Order;
import com.gotometrics.orderly.RowKey;
import org.apache.hadoop.io.Writable;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Defines a field that is part of an {@link IndexDefinition}.
 */
public abstract class IndexFieldDefinition implements Writable {
    private String name;
    private Order order;

    protected IndexFieldDefinition() {
        // hadoop serialization
    }

    public IndexFieldDefinition(String name) {
        this(name, Order.ASCENDING);
    }

    public IndexFieldDefinition(String name, Order order) {
        this.name = name;
        this.order = order;
    }

    public IndexFieldDefinition(String name, ObjectNode jsonObject) {
        this(name, jsonObject.get("order") != null ? Order.valueOf(jsonObject.get("order").getTextValue()) : null);
    }

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        Preconditions.checkNotNull(order, "Null argument: order");
        this.order = order;
    }

    public ObjectNode toJson() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode object = factory.objectNode();
        object.put("class", this.getClass().getName());
        object.put("order", this.order.toString());
        return object;
    }

    abstract RowKey asRowKey();

    /**
     * @return a rowKey with termination explicitly disabled (only to be used for prefix searches etc, never to be
     *         used for actually storing in HBase!)
     */
    abstract RowKey asRowKeyWithoutTermination();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        IndexFieldDefinition other = (IndexFieldDefinition) obj;

        if (!name.equals(other.name)) {
            return false;
        }

        if (order != other.order) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeUTF(order.name());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        name = in.readUTF();
        order = Order.valueOf(in.readUTF());
    }
}
