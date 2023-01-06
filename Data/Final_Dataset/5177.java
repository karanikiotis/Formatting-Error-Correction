/*
 * Copyright 2014 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Applies a Nonnull annotation to the whole package using JSR305's default annotation system,
 * but in a way that makes Nonnull inapplicable.
 */
@DefaultNonnullInapplicable
package nl.jqno.equalsverifier.integration.extra_features.nonnull.jsr305.inapplicable;

import nl.jqno.equalsverifier.testhelpers.annotations.DefaultNonnullInapplicable;
