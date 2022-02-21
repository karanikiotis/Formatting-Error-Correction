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
package jetbrains.mps.smodel.language;

import jetbrains.mps.smodel.runtime.ConceptPresentation;
import jetbrains.mps.smodel.runtime.ConceptPresentationAspect;
import org.jetbrains.mps.openapi.language.SAbstractConcept;

public class ConceptPropertiesRegistry {
  private LanguageRegistry myLanguageRegistry;

  public ConceptPropertiesRegistry(LanguageRegistry languageRegistry) {
    myLanguageRegistry = languageRegistry;
  }

  public ConceptPresentation getConceptProperties(SAbstractConcept concept) {
    LanguageRuntime languageRuntime = myLanguageRegistry.getLanguage(concept.getLanguage());
    if (languageRuntime == null) {
      return null;
    }
    ConceptPresentationAspect structureProps = languageRuntime.getAspect(ConceptPresentationAspect.class);
    if (structureProps == null) {
      return null;
    }
    return structureProps.getDescriptor(concept);
  }
}
