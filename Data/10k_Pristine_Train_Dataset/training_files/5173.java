/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.platform.database.HSQLPlatform;

public class CubaHSQLPlatform extends HSQLPlatform {
    @Override
    public boolean supportsNestingOuterJoins() {
        //nested joins supports in hsqldb from version 1.9
        //https://sourceforge.net/p/hsqldb/feature-requests/206/
        return true;
    }
}
