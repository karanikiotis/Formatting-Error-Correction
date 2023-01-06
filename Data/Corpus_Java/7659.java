/**
 * Copyright (c) 2014-2017, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;
import org.apache.commons.io.input.NullInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link Execution}.
 * @author Georgy Vlasov (wlasowegor@gmail.com)
 * @version $Id: 72eec660f624233551455fc95a1527f1b079c039 $
 * @since 1.4
 */
public final class ExecutionTest {

    /**
     * Exit code expected from a command.
     */
    public static final int EXIT_CODE = 127;

    /**
     * Tests that {@link Execution} executes a command and returns a correct
     * exit code.
     * @throws Exception If fails
     */
    @Test
    public void executesCommand() throws Exception {
        final Session session = Mockito.mock(Session.class);
        final ChannelExec channel = Mockito.mock(ChannelExec.class);
        Mockito.when(session.openChannel(Mockito.anyString()))
            .thenReturn(channel);
        Mockito.when(channel.isClosed()).thenReturn(Boolean.TRUE);
        Mockito.when(channel.getExitStatus()).thenReturn(EXIT_CODE);
        Assert.assertEquals(
            EXIT_CODE,
            new Execution.Default(
                "hello",
                new NullInputStream(0L),
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream(),
                session
            ).exec()
        );
    }
}
