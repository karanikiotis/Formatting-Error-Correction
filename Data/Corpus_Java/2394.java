/**
 *  Copyright (c) 2015-present, Jim Kynde Meyer
 *  All rights reserved.
 *
 *  This source code is licensed under the MIT license found in the
 *  LICENSE file in the root directory of this source tree.
 */
package com.intellij.lang.jsgraphql.endpoint.ide.intentions;

import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.lang.jsgraphql.endpoint.JSGraphQLEndpointTokenTypes;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class JSGraphQLEndpointCreateObjectTypeIntention extends JSGraphQLEndpointCreateDefinitionIntention implements HighPriorityAction {
    @NotNull
    @Override
    public String getText() {
        return "Create 'type'";
    }

    @Override
    protected IElementType getSupportedDefinitionType() {
        return JSGraphQLEndpointTokenTypes.TYPE;
    }
}
