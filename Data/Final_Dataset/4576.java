/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.contrib.hive;

import javax.validation.constraints.NotNull;

import com.datatorrent.lib.db.jdbc.JdbcStore;

/**
 * Hive Store that extends Jdbc Store and provides its own driver name.
 *
 * @since 2.1.0
 */
public class HiveStore extends JdbcStore
{
  public HiveStore()
  {
    super();
    this.setDatabaseDriver(HIVE_DRIVER);
  }

  public static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
  @NotNull
  public String filepath;

  public String getFilepath()
  {
    return filepath;
  }

  public void setFilepath(String filepath)
  {
    this.filepath = filepath;
  }

}
