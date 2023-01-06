/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.entity;

import org.mayocat.model.Entity;
import org.xwiki.component.annotation.Role;

/**
 * @version $Id: af8d1219b074217b50971e248e1868b0383353ca $
 */
@Role
public interface EntityLoader
{
    <E extends Entity> E load(String slug);

    <E extends Entity> E load(String slug, String tenantSlug);
}
