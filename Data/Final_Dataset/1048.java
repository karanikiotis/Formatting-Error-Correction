/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.store.filesystem.internal;

import java.io.File;

/**
 * A means of getting files for storing information about a given deleted document.
 *
 * @version $Id: b775a2cfedc1f31944f39db6246a4994d88af5de $
 * @since 9.0RC1
 */
public class DefaultDeletedDocumentContentFileProvider implements DeletedDocumentContentFileProvider
{
    /**
     * The file holding the content of the deleted document.
     */
    private static final String DELETED_DOCUMENT_FILE_NAME = "content.xml";

    /**
     * The directory where all information about this deleted document resides.
     */
    private final File deletedDocumentDir;

    /**
     * @param deletedDocumentDir the location where the information about the deleted document will be stored.
     */
    public DefaultDeletedDocumentContentFileProvider(final File deletedDocumentDir)
    {
        this.deletedDocumentDir = deletedDocumentDir;
    }

    @Override
    public File getDeletedDocumentContentFile()
    {
        return new File(this.deletedDocumentDir, DELETED_DOCUMENT_FILE_NAME);
    }
}
