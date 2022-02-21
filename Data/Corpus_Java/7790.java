/*
 * Copyright © 2014 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.cdap.common.service;

import co.cask.cdap.common.conf.CConfiguration;

/**
 * Provides interface for starting and stopping a server component.
 */
public interface Server {

  /**
   * Starts the {@link Server}.
   * @param args arguments for the service
   * @param conf instance of configuration object.
   *
   * @throws ServerException If there is an problem when starting the server.
   */
  void start(String[] args, CConfiguration conf) throws ServerException;

  /**
   * Stops the {@link Server}.
   * @param now true specifies non-graceful shutdown; false otherwise.
   *
   * @throws ServerException If there is an problem when stopping the server.
   */
  void stop(boolean now) throws ServerException;

}
