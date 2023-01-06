/*
Copyright 2011-2016 Google Inc. All Rights Reserved.

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
package com.google.security.zynamics.binnavi.debug.connection.packets.replyparsers;

import com.google.security.zynamics.binnavi.debug.connection.DebugCommandType;
import com.google.security.zynamics.binnavi.debug.connection.interfaces.ClientReader;
import com.google.security.zynamics.binnavi.debug.connection.packets.replies.SearchReply;

import java.io.IOException;

/**
 * Parser responsible for parsing replies to Search requests.
 */
public final class SearchParser extends AbstractReplyParser<SearchReply> {
  /**
   * Creates a new Search reply parser.
   *
   * @param clientReader Used to read messages sent by the debug client.
   */
  public SearchParser(final ClientReader clientReader) {
    super(clientReader, DebugCommandType.RESP_SEARCH_SUCCESS);
  }

  @Override
  protected SearchReply parseError(final int packetId) throws IOException {
    return new SearchReply(packetId, parseInteger(), null);
  }

  @Override
  public SearchReply parseSuccess(final int packetId, final int argumentCount) throws IOException {
    return new SearchReply(packetId, 0, parseAddress());
  }
}
