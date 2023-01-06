/*
 * Copyright (C) 2016 AriaLyy(https://github.com/AriaLyy/Aria)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arialyy.aria.core.inf;

import com.arialyy.aria.core.RequestEnum;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyy on 2017/2/23.
 */

public abstract class AbsTaskEntity {
  /**
   * http 请求头
   */
  public Map<String, String> headers = new HashMap<>();

  /**
   * 网络请求类型
   */
  public RequestEnum requestEnum = RequestEnum.GET;

  /**
   * 重定向后，新url的key
   */
  public String redirectUrlKey = "location";

  public abstract AbsEntity getEntity();
}
