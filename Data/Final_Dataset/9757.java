/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat;

import java.util.List;

import org.mayocat.meta.EntityMeta;

/**
 * @version $Id: 62d153e4bd46f7df301cf5dc4f4c9c183ef6ff75 $
 */
public interface Module
{
    String getName();

    List<EntityMeta> getEntities();
}
