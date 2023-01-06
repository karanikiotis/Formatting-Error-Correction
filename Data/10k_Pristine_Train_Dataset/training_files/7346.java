/*
 * Copyright (c) 2002-2012 Alibaba Group Holding Limited.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.citrus.service.mappingrule.support;

import static com.alibaba.citrus.springext.util.SpringExtUtil.*;
import static com.alibaba.citrus.util.ObjectUtil.*;
import static com.alibaba.citrus.util.StringUtil.*;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class AbstractModuleMappingRuleDefinitionParser<M extends AbstractModuleMappingRule> extends
                                                                                                     AbstractMappingRuleDefinitionParser<M> {
    @Override
    protected final void doParseMappingRule(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        attributesToProperties(element, builder, "moduleType");

        builder.addPropertyReference("moduleLoaderService",
                                     defaultIfNull(trimToNull(element.getAttribute("moduleLoaderServiceRef")), "moduleLoaderService"));

        doParseModuleMappingRule(element, parserContext, builder);
    }

    protected abstract void doParseModuleMappingRule(Element element, ParserContext parserContext,
                                                     BeanDefinitionBuilder builder);
}
