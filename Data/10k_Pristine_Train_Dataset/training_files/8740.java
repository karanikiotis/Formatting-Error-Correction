// Copyright (c) 2007-Present Pivotal Software, Inc.  All rights reserved.
//
// This software, the RabbitMQ Java client library, is triple-licensed under the
// Mozilla Public License 1.1 ("MPL"), the GNU General Public License version 2
// ("GPL") and the Apache License version 2 ("ASL"). For the MPL, please see
// LICENSE-MPL-RabbitMQ. For the GPL, please see LICENSE-GPL2.  For the ASL,
// please see LICENSE-APACHE2.
//
// This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
// either express or implied. See the LICENSE file for specific language governing
// rights and limitations of this software.
//
// If you have any questions regarding licensing, please contact us at
// info@rabbitmq.com.


package com.rabbitmq.tools.jsonrpc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.rabbitmq.tools.json.JSONUtil;

/**
 * Description of a single JSON-RPC procedure.
 */
public class ProcedureDescription {
    /** Procedure name */
    public String name;
    /** Human-readable procedure summary */
    public String summary;
    /** Human-readable instructions for how to get information on the procedure's operation */
    public String help;
    /** True if this procedure is idempotent, that is, can be accessed via HTTP GET */
    public boolean idempotent;

    /** Descriptions of parameters for this procedure */
    private ParameterDescription[] params;
    /** Return type for this procedure */
    private String returnType;

    /** Reflected method object, used for service invocation */
    private Method method;

    public ProcedureDescription(Map<String, Object> pm) {
        JSONUtil.tryFill(this, pm);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> p = (List<Map<String, Object>>) pm.get("params");
        params = new ParameterDescription[p.size()];
        int count = 0;
        for (Map<String, Object> param_map: p) {
            ParameterDescription param = new ParameterDescription(param_map);
            params[count++] = param;
        }
    }

    public ProcedureDescription(Method m) {
        this.method = m;
        this.name = m.getName();
        this.summary = "";
        this.help = "";
        this.idempotent = false;
        Class<?>[] parameterTypes = m.getParameterTypes();
        this.params = new ParameterDescription[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = new ParameterDescription(i, parameterTypes[i]);
        }
        this.returnType = ParameterDescription.lookup(m.getReturnType());
    }

    public ProcedureDescription() {
        // no work to do here
    }

    /** Getter for return type */
    public String getReturn() { return returnType; }
    /** Private API - used via reflection during parsing/loading */
    public void setReturn(String value) { returnType = value; }

    /** Private API - used to get the reflected method object, for servers */
    public Method internal_getMethod() { return method; }

    /** Gets an array of parameter descriptions for all this procedure's parameters */
    public ParameterDescription[] internal_getParams() {
        return params;
    }

    /** Retrieves the parameter count for this procedure */
    public int arity() {
        return (params == null) ? 0 : params.length;
    }

    public ParameterDescription[] getParams() {
        return params;
    }
}
