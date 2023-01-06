/*
* Copyright 2015 Stormpath, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.stormpath.sdk.impl.oauth;

import com.stormpath.sdk.impl.ds.InternalDataStore;
import com.stormpath.sdk.oauth.RefreshToken;
import com.stormpath.sdk.oauth.TokenTypeHint;

import java.util.Map;

/**
 * @since 1.0.RC7
 */
public class DefaultRefreshToken extends AbstractBaseOAuthToken implements RefreshToken {

    public DefaultRefreshToken(InternalDataStore dataStore) {
        super(dataStore);
    }

    public DefaultRefreshToken(InternalDataStore dataStore, Map<String, Object> properties) {
        super(dataStore, properties);
    }

    @Override
    protected TokenTypeHint getTokenTypeHint() {
        return TokenTypeHint.REFRESH_TOKEN;
    }
}
