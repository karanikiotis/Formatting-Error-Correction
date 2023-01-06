//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.abstrstyling;

import limelight.styles.compiling.RealStyleAttributeCompilerFactory;

public interface StyleAttributeCompilerFactory
{
  RealStyleAttributeCompilerFactory compilerFactory = null;

  StyleCompiler compiler(String type, String name);

}
