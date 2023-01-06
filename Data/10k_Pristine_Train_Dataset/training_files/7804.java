package acceptance;

import com.google.common.base.Throwables;

import org.junit.Rule;
import org.junit.Test;
import io.digdag.core.database.DataSourceProvider;
import utils.TemporaryDigdagServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.Statement;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import static utils.TestUtils.expect;

public class ServerJmxIT
{
    public static final Pattern JMX_PORT_PATTERN = Pattern.compile("\\s*JMX agent started on port (\\d+)\\s*");

    @Rule
    public TemporaryDigdagServer server = TemporaryDigdagServer.builder()
            .inProcess(false)
            .configuration(
                    "server.jmx.port=0")
            .build();

    private static JMXConnector connectJmx(TemporaryDigdagServer server)
        throws IOException
    {
        Matcher matcher = JMX_PORT_PATTERN.matcher(server.outUtf8());
        assertThat(matcher.find(), is(true));
        int port = Integer.parseInt(matcher.group(1));

        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
            return JMXConnectorFactory.connect(url, null);
        }
        catch (MalformedURLException ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Test
    public void verifyJmx()
            throws Exception
    {
        try (JMXConnector con = connectJmx(server)) {
            MBeanServerConnection beans = con.getMBeanServerConnection();

            Object uptime = beans.getAttribute(ObjectName.getInstance("java.lang", "type", "Runtime"), "Uptime");
            assertThat(uptime, instanceOf(Long.class));

            Object enqueueCount = beans.getAttribute(ObjectName.getInstance("io.digdag.core.workflow", "name", "TaskQueueDispatcher"), "EnqueueCount");
            assertThat(enqueueCount, is(0L));
        }
    }

    @Test
    public void verifyUncaughtErrorCount()
            throws Exception
    {
        assumeThat(server.isRemoteDatabase(), is(true));

        try (JMXConnector con = connectJmx(server)) {
            MBeanServerConnection beans = con.getMBeanServerConnection();

            Object uncaughtErrorCount = beans.getAttribute(ObjectName.getInstance("io.digdag.core", "name", "ErrorReporter"), "UncaughtErrorCount");
            assertThat(uncaughtErrorCount, is(0));

            // oops, tasks table is broken!?
            try (DataSourceProvider dsp = new DataSourceProvider(server.getRemoteTestDatabaseConfig())) {
                Statement stmt = dsp.get().getConnection().createStatement();
                stmt.execute("drop table tasks cascade");
            }

            // should increment uncaught exception count
            expect(Duration.ofMinutes(5), () -> {
                int count = (int) beans.getAttribute(ObjectName.getInstance("io.digdag.core", "name", "ErrorReporter"), "UncaughtErrorCount");
                return count > 0;
            });
        }
    }
}
