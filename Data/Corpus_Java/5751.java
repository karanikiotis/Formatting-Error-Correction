/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx.javash.kernel;

import com.twosigma.beakerx.KernelSocketsServiceTest;
import com.twosigma.beakerx.javash.evaluator.JavaEvaluator;
import com.twosigma.beakerx.javash.kernel.Java;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.kernel.KernelParameters;
import com.twosigma.beakerx.kernel.KernelRunner;
import com.twosigma.beakerx.message.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.twosigma.MessageAssertions.verifyExecuteReplyMessage;
import static com.twosigma.beakerx.MessageFactoryTest.getExecuteRequestMessage;
import static com.twosigma.beakerx.evaluator.EvaluatorResultTestWatcher.waitForResultAndReturnIdleMessage;
import static com.twosigma.beakerx.kernel.comm.KernelControlSetShellHandler.CLASSPATH;
import static com.twosigma.beakerx.kernel.comm.KernelControlSetShellHandler.IMPORTS;
import static org.assertj.core.api.Assertions.assertThat;

public class JavaKernelTest {

  private Java kernel;
  private KernelSocketsServiceTest kernelSocketsService;

  @Before
  public void setUp() throws Exception {
    String sessionId = "sessionId2";
    JavaEvaluator evaluator = new JavaEvaluator(sessionId, sessionId);
    kernelSocketsService = new KernelSocketsServiceTest();
    kernel = new Java(sessionId, evaluator, kernelSocketsService);
    kernel.setShellOptions(kernelParameters());
    new Thread(() -> KernelRunner.run(() -> kernel)).start();
    kernelSocketsService.waitForSockets();
  }

  @After
  public void tearDown() throws Exception {
    kernelSocketsService.shutdown();
  }

  @Test
  public void evaluate() throws Exception {
    //given
    String code = "return 1;";
    Message message = getExecuteRequestMessage(code);
    //when
    kernelSocketsService.handleMsg(message);
    kernelSocketsService.handleMsg(message);
    Optional<Message> idleMessage = waitForResultAndReturnIdleMessage(kernelSocketsService.getKernelSockets());
    //then
    assertThat(idleMessage).isPresent();
    verifyPublishedMsgs(kernelSocketsService);
    verifySentMsgs(kernelSocketsService);
    verifyResult(kernelSocketsService.getExecuteResultMessage().get());
  }

  private void verifyPublishedMsgs(KernelSocketsServiceTest service) {
    assertThat(service.getBusyMessage()).isPresent();
    assertThat(service.getExecuteInputMessage()).isPresent();
    assertThat(service.getExecuteResultMessage()).isPresent();
    assertThat(service.getIdleMessage()).isPresent();
  }

  private void verifySentMsgs(KernelSocketsServiceTest service) {
    verifyExecuteReplyMessage(service.getReplyMessage());
  }

  private void verifyResult(Message result) {
    Map actual = ((Map) result.getContent().get(Comm.DATA));
    String value = (String) actual.get("text/plain");
    assertThat(value).isEqualTo("1");
  }

  private KernelParameters kernelParameters() {
    Map<String, Object> params = new HashMap<>();
    params.put(IMPORTS, new ArrayList<>());
    params.put(CLASSPATH, new ArrayList<>());
    return new KernelParameters(params);
  }

}