/*
 * Jopr Management Platform
 * Copyright (C) 2005-2009 Red Hat, Inc.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2, as
 * published by the Free Software Foundation, and/or the GNU Lesser
 * General Public License, version 2.1, also as published by the Free
 * Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.rhq.plugins.jbossas5.util;

import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.definition.ConfigurationDefinition;
import org.rhq.core.domain.configuration.definition.ConfigurationTemplate;
import org.rhq.core.domain.measurement.MeasurementDefinition;
import org.rhq.core.domain.operation.OperationDefinition;
import org.rhq.core.domain.resource.ResourceType;

/**
 * @author Ian Springer
 */
public class ResourceTypeUtils
{
    public static Configuration getDefaultPluginConfiguration(ResourceType resourceType)
    {
        ConfigurationDefinition pluginConfigurationDefinition = resourceType.getPluginConfigurationDefinition();
        if (pluginConfigurationDefinition != null)
        {
            ConfigurationTemplate template = pluginConfigurationDefinition.getDefaultTemplate();
            if (template != null)
                return template.getConfiguration().deepCopy();
        }
        return new Configuration(); // there is no default plugin config defined - return an empty one
    }

    /**
     * TODO
     *
     * @param resourceType
     * @param metricName
     * @return
     */
    @Nullable
    public static MeasurementDefinition getMeasurementDefinition(ResourceType resourceType, String metricName)
    {
        Set<MeasurementDefinition> metricDefinitions = resourceType.getMetricDefinitions();
        for (MeasurementDefinition metricDefinition : metricDefinitions)
        {
            if (metricDefinition.getName().equals(metricName))
                return metricDefinition;
        }
        return null;
    }

    /**
     * TODO
     *
     * @param resourceType
     * @param operationName
     * @return
     */
    @Nullable
    public static OperationDefinition getOperationDefinition(ResourceType resourceType, String operationName)
    {
        Set<OperationDefinition> operationDefinitions = resourceType.getOperationDefinitions();
        for (OperationDefinition operationDefinition : operationDefinitions)
        {
            if (operationDefinition.getName().equals(operationName))
                return operationDefinition;
        }
        return null;
    }

    private ResourceTypeUtils()
    {
    }
}
