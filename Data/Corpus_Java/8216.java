/*
 * Copyright 2003-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.mps.generator.impl;

import jetbrains.mps.generator.template.TemplateQueryContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.mps.openapi.model.SNodeReference;

/**
 * Serves as designated indicator of an error in a query code, to tell errors of MPS from that of language designer.
 * It's not intended to be instantiated by client code, it's MPS own reaction to an exception that originates in user's code.
 * It has both descriptive message and nested exception.
 * @author Artem Tikhomirov
 * @since 3.5
 */
public class TemplateQueryException extends GenerationFailureException {
  @Nullable
  private TemplateQueryContext myQueryContext;

  public TemplateQueryException(@NotNull String message, @NotNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Updates {@link #setTemplateModelLocation(SNodeReference) template model location} from the context, if not already set.
   * @param queryContext optional extra information about failed query
   */
  public void setQueryContext(@Nullable TemplateQueryContext queryContext) {
    if (queryContext != null && getTemplateModelLocation() == null) {
      setTemplateModelLocation(queryContext.getTemplateReference());
    }
    myQueryContext = queryContext;
  }

  /**
   * @return extra information about failed query, optional
   */
  @Nullable
  public TemplateQueryContext getQueryContext() {
    return myQueryContext;
  }
}
