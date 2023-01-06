/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.views;

import org.xwiki.component.annotation.Role;

/**
 * @version $Id: 1efa46e81dbd8d629750f21af54025c380409440 $
 */
@Role
public interface TemplateEngine
{
    void register(Template template) throws TemplateEngineException;

    String render(String templateName, String json) throws TemplateEngineException;
}
