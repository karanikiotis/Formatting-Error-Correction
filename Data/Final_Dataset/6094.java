/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.meta;

import java.util.List;

import org.xwiki.component.annotation.Role;

/**
 * @version $Id: 336f3d5b6abef93c6a10ab59ffba3fa22b73288a $
 */
@Role
public interface EntityMetaRegistry
{
    List<EntityMeta> getEntities();
}
