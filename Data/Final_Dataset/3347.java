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
package org.xwiki.filter.xar.internal.input;

import javax.inject.Singleton;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xwiki.component.annotation.Component;
import org.xwiki.filter.FilterException;
import org.xwiki.filter.xar.input.XARInputProperties;

/**
 * @version $Id: 07aaf323f61cc0354b9b253d80236b954bd8fe1a $
 * @since 9.0RC1
 */
@Component
@Singleton
public class WikiObjectPropertyReader extends AbstractWikiObjectPropertyReader
    implements XARXMLReader<WikiObjectPropertyReader.WikiObjectProperty>
{
    @Override
    public WikiObjectProperty read(XMLStreamReader xmlReader, XARInputProperties properties)
        throws XMLStreamException, FilterException
    {
        return readObjectProperty(xmlReader, properties, null);
    }
}
