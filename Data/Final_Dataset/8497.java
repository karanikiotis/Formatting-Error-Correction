/**
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.maven.dependencies;

import static org.junit.Assert.assertEquals;

import org.apache.maven.model.Exclusion;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MavenDependencyAdapterTest
{
   org.apache.maven.model.Dependency mvnDep;
   Dependency forgeDep;

   public MavenDependencyAdapterTest()
   {
      forgeDep = DependencyBuilder.create()
               .setArtifactId("seam-forge")
               .setGroupId("org.jboss.forge")
               .addExclusion(CoordinateBuilder.create()
                        .setArtifactId("sub-module")
                        .setGroupId("org.jboss.forge"))
               .addExclusion(CoordinateBuilder.create()
                        .setArtifactId("sub-module-2")
                        .setGroupId("org.jboss.forge"))
               .setScopeType("compile")
               .setVersion("9")
               .setPackaging("war");

      mvnDep = new org.apache.maven.model.Dependency();
      mvnDep.setArtifactId("seam-forge");
      mvnDep.setGroupId("org.jboss.forge");
      mvnDep.setVersion("9");
      mvnDep.setScope("ComPiLe");

      Exclusion ex1 = new Exclusion();
      ex1.setArtifactId("sub-module");
      ex1.setGroupId("org.jboss.forge");
      mvnDep.addExclusion(ex1);

      Exclusion ex2 = new Exclusion();
      ex2.setArtifactId("sub-module-2");
      ex2.setGroupId("org.jboss.forge");
      mvnDep.addExclusion(ex2);
   }

   @Test
   public void testConvertFromMVNToForge() throws Exception
   {
      MavenDependencyAdapter toForge = new MavenDependencyAdapter(mvnDep);
      MavenDependencyAdapter toMvn = new MavenDependencyAdapter(forgeDep);

      assertEquals(toForge.getArtifactId(), toMvn.getArtifactId());
      assertEquals(toForge.getGroupId(), toMvn.getGroupId());
      assertEquals(toForge.getVersion(), toMvn.getVersion());
      assertEquals(toForge.getScopeType(), toMvn.getScopeType());
      assertEquals(toForge.getScope(), toMvn.getScope());
   }

   @Test
   public void testExclusionsConvertProperly() throws Exception
   {
      MavenDependencyAdapter toForge = new MavenDependencyAdapter(mvnDep);
      MavenDependencyAdapter toMvn = new MavenDependencyAdapter(forgeDep);

      assertEquals(toForge.getExcludedCoordinates(), toMvn.getExcludedCoordinates());
      assertEquals(toForge.getExclusions().get(0).getArtifactId(), toMvn.getExclusions().get(0).getArtifactId());
      assertEquals(toForge.getExclusions().get(0).getGroupId(), toMvn.getExclusions().get(0).getGroupId());
      assertEquals(toForge.getExclusions().get(1).getArtifactId(), toMvn.getExclusions().get(1).getArtifactId());
      assertEquals(toForge.getExclusions().get(1).getGroupId(), toMvn.getExclusions().get(1).getGroupId());
   }
}
