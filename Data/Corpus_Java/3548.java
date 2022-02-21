/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.android.support.exopackage;

import android.app.Application;
import android.content.res.Configuration;

/**
 * This interface is used to delegate calls from main Application object.
 *
 * Implementations of this interface must have a one-argument constructor that takes
 * an argument of type {@link Application}.
 */
public interface ApplicationLike {

  /**
   * Same as {@link Application#onCreate()}.
   */
  void onCreate();

  /**
   * Same as {@link Application#onLowMemory()}.
   */
  void onLowMemory();

  /**
   * Same as {@link Application#onTrimMemory(int level)}.
   * @param level
   */
  void onTrimMemory(int level);

  /**
   * Same as {@link Application#onTerminate()}.
   */
  void onTerminate();

  /**
   * Same as {@link Application#onConfigurationChanged(Configuration newconfig)}.
   */
  void onConfigurationChanged(Configuration newConfig);

  /**
   * Same as {@link Application#getSystemService(String name)}.
   */
  Object getSystemService(String name);

}
