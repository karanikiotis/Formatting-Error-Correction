/*
 * Copyright 2012 NGDATA nv
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
package org.lilyproject.clientmetrics;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lilyproject.util.io.Closer;

public class JmxConnections {
    private Map<String, JMXConnector> connections = new HashMap<String, JMXConnector>();

    public JMXConnector getConnector(String serverName, String port) throws IOException {
        String hostport = serverName + ":" + port;
        JMXConnector connector = connections.get(hostport);
        if (connector == null) {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + hostport + "/jndi/rmi://" + hostport + "/jmxrmi");
            connector = JMXConnectorFactory.connect(url);
            connector.connect();
            connections.put(hostport, connector);
        }
        return connector;
    }

    public void close() {
        List<JMXConnector> list = new ArrayList<JMXConnector>(connections.values());
        connections.clear();

        for (JMXConnector conn : list) {
            Closer.close(conn);
        }
    }
}
