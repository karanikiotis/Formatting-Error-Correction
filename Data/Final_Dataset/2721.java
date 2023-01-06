/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2013-2016 tarent solutions GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/**
 *  <p>org.osiam.resources is a group of groups related the resource-server functionality.</p>
 *
 *  <p>The resource-server of OSIAM is build on top of SCIM v2 (see http://tools.ietf.org/html/draft-ietf-scim-api-01,
 *  http://tools.ietf.org/html/draft-ietf-scim-core-schema-01 for further details on the standard).</p>
 *
 *  <p>The api is build on http and contains:</p>
 *
 *  <p>/ - org.osiam.resources.controller.RootController - would be a resource independent search but it is currently
 *  disabled.</p>
 *
 *  <p>/Group - org.osiam.resources.controller.GroupController - is a Controller to create, replace, modify, get,
 *  delete and search groups.</p>
 *
 *  <p>/User - org.osiam.resources.controller.UserController - is a Controller to create, replace, modify, get,</p>
 *  delete and search user.
 *
 * <p>/ServiceProviderConfig - org.osiam.resources.controller.ServiceProviderConfigController is a controller to get
 * information about the running OSIAM instance.</p>
 *
 */
package org.osiam.resources;
