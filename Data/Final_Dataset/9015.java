/**
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.projects.mock;

import java.util.Arrays;

import org.jboss.forge.addon.projects.AbstractProjectType;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;

/**
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class MockDisabledProjectType extends AbstractProjectType
{
    @Override
    public String getType()
    {
        return "disabled-mock";
    }

    @Override
    public Class<? extends UIWizardStep> getSetupFlow()
    {
        return null;
    }

    @Override
    public Iterable<Class<? extends ProjectFacet>> getRequiredFacets()
    {
        return Arrays.<Class<? extends ProjectFacet>> asList(MetadataFacet.class);
    }

    @Override
    public String toString()
    {
        return getType();
    }

    @Override
    public int priority()
    {
        return 0;
    }

    @Override
    public boolean isEnabled(UIContext context)
    {
        return false;
    }
}
