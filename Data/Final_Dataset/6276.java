/*
   Copyright (c) 2012 LinkedIn Corp.

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

/**
 * $Id: $
 */

package com.linkedin.d2.balancer.util.hashing;

import com.linkedin.d2.balancer.ServiceUnavailableException;
import com.linkedin.d2.balancer.util.MapKeyResult;

import java.net.URI;
import java.util.Map;

/**
 * @author Josh Walker
 * @version $Revision: $
 */

public interface HashRingProvider
{
  /**
   * Obtain the hash ring for a given service URI.
   * @param serviceUri - the URI of the service for which the hash rings is being requested.
   * @param keys - the set of keys to query
   * @return @link MapKeyResult, keys mapped to partitions, unmapped keys are also returned with error types
   * @throws ServiceUnavailableException - if the service identified by the given URI is not available, i.e. map is empty.
   * @throws IllegalStateException - if this HashRingProvider is not configured with a valid hash rings.                                                                                                     L
   */
  public <K> MapKeyResult<Ring<URI>, K> getRings(URI serviceUri, Iterable<K> keys) throws ServiceUnavailableException;

  /**
   * Obtain the hash ring for a given service URI.
   * @param serviceUri - the URI of the service for which the hash ring is being requested.
   * @return a map of partitionId to hash ring of the partition; a map entry will be created for each partition even if
   * there is currently no server serving the partition (empty ring in this case)
   * @throws ServiceUnavailableException - if the service identified by the given URI is not available.
   * @throws IllegalStateException - if this HashRingProvider is not configured with a valid hash ring.
   */
  public Map<Integer, Ring<URI>> getRings(URI serviceUri) throws ServiceUnavailableException;

}
