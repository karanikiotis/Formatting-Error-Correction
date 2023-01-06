package org.testcontainers.junit;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.utility.TestEnvironment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.rnorth.visibleassertions.VisibleAssertions.info;
import static org.rnorth.visibleassertions.VisibleAssertions.pass;

/**
 * Created by rnorth on 11/06/2016.
 */
public class DockerComposePassthroughTest {

    @BeforeClass
    public static void checkVersion() {
        Assume.assumeTrue(TestEnvironment.dockerApiAtLeast("1.22"));
    }

    @Rule
    public DockerComposeContainer compose =
            new DockerComposeContainer(new File("src/test/resources/v2-compose-test-passthrough.yml"))
                    .withEnv("foo", "bar")
                    .withExposedService("alpine_1", 3000);

    @Test(timeout = 30_000)
    public void testEnvVar() throws IOException {
        BufferedReader br = Unreliables.retryUntilSuccess(10, TimeUnit.SECONDS, () -> {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            Socket socket = new Socket(compose.getServiceHost("alpine_1", 3000), compose.getServicePort("alpine_1", 3000));
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        });

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            while (br.ready()) {
                String line = br.readLine();
                if (line.contains("bar=bar")) {
                    pass("Mapped environment variable was found");
                    return true;
                }
            }
            info("Mapped environment variable was not found yet - process probably not ready");
            Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
            return false;
        });

    }
}
