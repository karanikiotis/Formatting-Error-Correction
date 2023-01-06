/*
 Copyright (C) 2015 Electronic Arts Inc.  All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1.  Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2.  Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3.  Neither the name of Electronic Arts, Inc. ("EA") nor the names of
     its contributors may be used to endorse or promote products derived
     from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY ELECTRONIC ARTS AND ITS CONTRIBUTORS "AS IS" AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL ELECTRONIC ARTS OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ea.async.maven.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mojo(name = "instrument",
        defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresProject = false,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
@Execute(goal = "instrument", phase = LifecyclePhase.PROCESS_CLASSES)
public class MainMojo extends AbstractAsyncMojo
{


    protected String getType()
    {
        return "main-classes";
    }

    /**
     * Return the main classes directory
     */
    protected File getClassesDirectory()
    {
        return classesDirectory;
    }

    @Override
    public void execute() throws MojoExecutionException
    {
        super.execute();
    }

    private List<String> generateClassPath()
    {
        List<String> classpath = new ArrayList<>(2 + project.getArtifacts().size());

        classpath.add(classesDirectory.getAbsolutePath());

        Set<Artifact> classpathArtifacts = project.getArtifacts();

        for (Artifact artifact : classpathArtifacts)
        {
            if (artifact.getArtifactHandler().isAddedToClasspath()
                    && !"test".equalsIgnoreCase(artifact.getScope()))
            {
                File file = artifact.getFile();
                if (file != null)
                {
                    classpath.add(file.getPath());
                }
            }
        }
        return classpath;
    }

    @Override
    protected ClassLoader createClassLoader()
    {
        final URL[] urls = generateClassPath().stream().map(f -> {
            try
            {
                return new File(f).toURI().toURL();
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException(e);
            }
        }).toArray(s -> new URL[s]);
        return new URLClassLoader(urls);
    }
}
