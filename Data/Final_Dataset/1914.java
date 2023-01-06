/*
 * Copyright 2013 Real Logic Ltd.
 * Copyright (C) 2017 MarketFactory, Inc
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
package uk.co.real_logic.sbe.generation.csharp;

import org.agrona.generation.OutputManager;
import org.agrona.Verify;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static java.io.File.separatorChar;

/**
 * {@link OutputManager} for managing the creation of C# source files
 * as the target of code generation.
 * <p>
 * The character encoding for the {@link java.io.Writer} is UTF-8.
 */
public class CSharpNamespaceOutputManager implements OutputManager
{
    private final File outputDir;

    /**
     * Create a new {@link OutputManager} for generating C# source
     * files into a given package.
     *
     * @param baseDirectoryName for the generated source code.
     * @param packageName       for the generated source code relative to the baseDirectoryName.
     * @throws IOException if an error occurs during output
     */
    public CSharpNamespaceOutputManager(final String baseDirectoryName, final String packageName) throws IOException
    {
        Verify.notNull(baseDirectoryName, "baseDirectoryName");
        Verify.notNull(packageName, "packageName");

        final String dirName =
            (baseDirectoryName.endsWith("" + separatorChar) ? baseDirectoryName : baseDirectoryName + separatorChar) +
                packageName.replace('.', '_');

        outputDir = new File(dirName);
        if (!outputDir.exists() && !outputDir.mkdirs())
        {
            throw new IllegalStateException("Unable to create directory: " + dirName);
        }
    }

    /**
     * Create a new output which will be a C# source file in the given package.
     * <p>
     * The {@link java.io.Writer} should be closed once the caller has finished with it. The Writer is
     * buffer for efficient IO operations.
     *
     * @param name the name of the C# class.
     * @return a {@link java.io.Writer} to which the source code should be written.
     */
    public Writer createOutput(final String name) throws IOException
    {
        final File targetFile = new File(outputDir, name + ".cs");
        return Files.newBufferedWriter(targetFile.toPath(), StandardCharsets.UTF_8);
    }
}
