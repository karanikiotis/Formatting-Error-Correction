/*
 * Copyright 2003-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.mps.jps.make.tests;

import com.intellij.testFramework.TestDataFile;
import com.intellij.testFramework.TestDataPath;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildResult;


public class MakeAfterRebuildDoesNothingTestCase extends MpsJpsSingleTestCase {
  @Override
  protected void doTest(@NotNull @TestDataFile @NonNls String testName) {
    doTestRebuild(testName);
    final BuildResult buildResult = doMake(false);
    buildResult.assertUpToDate();
  }

  // FIXME: fails because we cannot deal with the MPS generated files creation under the model source root which is not marked as isGenerated=true
  // FIXME: ignored for now
  @Override
  public void testJavaSourceGenNearModels() {
//    doTest("javaSourceGenNearModels/in");
  }
  // FIXME: see the comment above; ignored for now
  @Override
  public void testXmlSourceGenNearModels() {
//    doTest("xmlSourceGenNearModels/in");
  }
}
