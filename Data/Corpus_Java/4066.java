// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.collide.dto;

import com.google.collide.dtogen.shared.RoutingType;
import com.google.collide.dtogen.shared.ServerToClientDto;
import com.google.collide.json.shared.JsonArray;

/**
 * Response for requesting project members, and users requesting membership.
 */
@RoutingType(type = RoutingTypes.GETPROJECTMEMBERSRESPONSE)
public interface GetProjectMembersResponse extends ServerToClientDto {

  /**
   * Returns the list of current project members. This should always be
   * non-null.
   */
  JsonArray<UserDetailsWithRole> getMembers();

  /**
   * Returns the list of users requesting project membership. This should always
   * be non-null.
   */
  JsonArray<UserDetailsWithRole> getPendingMembers();
}
