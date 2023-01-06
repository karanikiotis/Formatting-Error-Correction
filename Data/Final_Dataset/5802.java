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
package com.xpn.xwiki.internal.filter.output;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.filter.instance.internal.InstanceUtils;
import org.xwiki.filter.instance.internal.output.AbstractBeanOutputInstanceFilterStreamFactory;
import org.xwiki.filter.instance.output.DocumentInstanceOutputProperties;

import com.xpn.xwiki.internal.filter.DocumentInstanceFilter;

/**
 * @version $Id: a999f2a38ce4303da99385efd13ac5abc9533b1f $
 * @since 6.2M1
 */
@Component
@Named(DocumentInstanceOutputFilterStreamFactory.ROLEHINT)
@Singleton
public class DocumentInstanceOutputFilterStreamFactory
    extends AbstractBeanOutputInstanceFilterStreamFactory<DocumentInstanceOutputProperties, DocumentInstanceFilter>
{
    /**
     * The id of this {@link org.xwiki.filter.instance.output.OutputInstanceFilterStreamFactory}.
     */
    public static final String ID = "document";

    /**
     * The role hint of this {@link org.xwiki.filter.output.OutputFilterStreamFactory}.
     */
    public static final String ROLEHINT = InstanceUtils.ROLEHINT + '+' + ID;

    /**
     * The default constructor.
     */
    public DocumentInstanceOutputFilterStreamFactory()
    {
        super(ID);

        setName("XWiki documents instance output stream");
        setDescription("Specialized version of the XWiki instance output stream for documents.");
    }
}
