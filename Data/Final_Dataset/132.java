/*
   Copyright (c) 2014 LinkedIn Corp.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.linkedin.restli.common;


import com.linkedin.data.template.RecordTemplate;

import java.util.Map;
import java.util.Set;


/**
 * The properties of the resource which are common to all resource types and apply to all methods of
 * resource implementation. The implementations of this interface must be immutable.
 */
public interface ResourceProperties
{
  Set<ResourceMethod> getSupportedMethods();

  TypeSpec<?> getKeyType();

  TypeSpec<? extends RecordTemplate> getValueType();

  ComplexKeySpec<? extends RecordTemplate, ? extends RecordTemplate> getComplexKeyType();

  Map<String, CompoundKey.TypeInfo> getKeyParts();

  boolean isKeylessResource();
}
