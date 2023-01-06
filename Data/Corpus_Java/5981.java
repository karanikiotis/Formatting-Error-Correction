/*
 * Copyright © 2014 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package co.cask.cdap.examples.countrandom;

import co.cask.cdap.api.annotation.ProcessInput;
import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.OutputEmitter;

/**
 * Number splitter Flowlet {@code NumberSplitter}.
 */
public class NumberSplitter extends AbstractFlowlet {
  private OutputEmitter<Integer> output;

  @ProcessInput
  public void process(Integer number)  {
    emit(number % 10000);
    emit(number % 1000);
    emit(number % 100);
    emit(number % 10);
  }

  private void emit(Integer i) {
    // emit i with a hash key named "n" and value i
    output.emit(i, "n", i);
  }
}

