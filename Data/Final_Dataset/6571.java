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

package com.linkedin.restli.server.resources;


import com.linkedin.data.template.RecordTemplate;
import com.linkedin.restli.common.CompoundKey;
import com.linkedin.restli.common.PatchRequest;
import com.linkedin.restli.server.BatchCreateRequest;
import com.linkedin.restli.server.BatchCreateResult;
import com.linkedin.restli.server.BatchDeleteRequest;
import com.linkedin.restli.server.BatchPatchRequest;
import com.linkedin.restli.server.BatchUpdateRequest;
import com.linkedin.restli.server.BatchUpdateResult;
import com.linkedin.restli.server.PagingContext;
import com.linkedin.restli.server.RoutingException;
import com.linkedin.restli.server.UpdateResponse;
import com.linkedin.restli.server.annotations.RestLiAssociation;
import com.linkedin.restli.server.annotations.RestLiTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Base {@link AssociationResource} implementation. All implementations should extend this
 *
 * @author dellamag
 */
@RestLiTemplate(expectedAnnotation = RestLiAssociation.class)
public class AssociationResourceTemplate<V extends RecordTemplate> extends
    ResourceContextHolder implements AssociationResource<V>
{
  /** @see com.linkedin.restli.server.resources.AssociationResource#getAll(com.linkedin.restli.server.PagingContext) */
  @Override
  public List<V> getAll(PagingContext pagingContext)
  {
    throw new RoutingException("'getAll(PagingContext)' not implemented", 400);
  }

  /**
   * @see com.linkedin.restli.server.resources.AssociationResource#batchGet(java.util.Set)
   */
  @Override
  public Map<CompoundKey, V> batchGet(final Set<CompoundKey> ids)
  {
    throw new RoutingException("'batch_get' not implemented", 400);
  }

  /**
   * @see AssociationResource#get
   */
  @Override
  public V get(final CompoundKey key)
  {
    throw new RoutingException("'get' not implemented", 400);
  }

  /**
   * @see AssociationResource#update
   */
  @Override
  public UpdateResponse update(final CompoundKey key, final V entity)
  {
    throw new RoutingException("'update' not implemented", 400);
  }

  /**
   * @see AssociationResource#update
   */
  @Override
  public UpdateResponse update(final CompoundKey key, final PatchRequest<V> patch)
  {
    throw new RoutingException("'update' not implemented", 400);
  }

  /**
   * @see AssociationResource#delete
   */
  @Override
  public UpdateResponse delete(final CompoundKey key)
  {
    throw new RoutingException("'delete' not implemented", 400);
  }

  @Override
  public BatchUpdateResult<CompoundKey, V> batchUpdate(final BatchUpdateRequest<CompoundKey, V> entities)
  {
    throw new RoutingException("'batch_update' not implemented", 400);
  }

  @Override
  public BatchUpdateResult<CompoundKey, V> batchUpdate(final BatchPatchRequest<CompoundKey, V> patches)
  {
    throw new RoutingException("'batch_partial_update' not implemented", 400);
  }

  @Override
  public BatchCreateResult<CompoundKey, V> batchCreate(final BatchCreateRequest<CompoundKey, V> entities)
  {
    throw new RoutingException("'batch_create' not implemented", 400);
  }

  @Override
  public BatchUpdateResult<CompoundKey, V> batchDelete(final BatchDeleteRequest<CompoundKey, V> ids)
  {
    throw new RoutingException("'batch_delete' not implemented", 400);
  }
}
