/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.grammar.java;

import java.net.URL;

public class OuterClass
{
   public class MockClass
   {
      private String field;
      private URL urlField;

      public String valueOf(URL url)
      {
         return url.getPath();
      }
   }
}
