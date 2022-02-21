/*
 * Copyright 2017, Google Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opencensus.tags;

import static com.google.common.base.Preconditions.checkArgument;

import io.opencensus.internal.StringUtil;
import io.opencensus.tags.TagKey.TagType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 * A map from keys to values that can be used to label anything that is associated with a specific
 * operation.
 *
 * <p>For example, {@code TagContext}s can be used to label stats, log messages, or debugging
 * information.
 */
@Immutable
public final class TagContext {
  /** The maximum length for a string tag value. */
  public static final int MAX_STRING_LENGTH = StringUtil.MAX_LENGTH;

  // The types of the TagKey and value must match for each entry.
  private final Map<TagKey<?>, Object> tags;

  TagContext(Map<TagKey<?>, Object> tags) {
    this.tags = Collections.unmodifiableMap(new HashMap<TagKey<?>, Object>(tags));
  }

  Map<TagKey<?>, Object> getTags() {
    return tags;
  }

  /**
   * Returns a builder based on this {@code TagContext}.
   *
   * @return a builder based on this {@code TagContext}.
   */
  public Builder toBuilder() {
    return new Builder(getTags());
  }

  /** Builder for the {@link TagContext} class. */
  public static final class Builder {
    private final Map<TagKey<?>, Object> tags;

    private Builder(Map<TagKey<?>, Object> tags) {
      this.tags = new HashMap<TagKey<?>, Object>(tags);
    }

    Builder() {
      this.tags = new HashMap<TagKey<?>, Object>();
    }

    /**
     * Adds the key/value pair regardless of whether the key is present.
     *
     * @param key the {@code TagKey} which will be set.
     * @param value the value to set for the given key.
     * @return this
     * @throws IllegalArgumentException if either argument is null, the key is the wrong type, or
     *     the value contains unprintable characters or is longer than {@link
     *     TagContext#MAX_STRING_LENGTH}.
     */
    public Builder set(TagKey<String> key, String value) {
      checkArgument(key.getTagType() == TagType.TAG_STRING);

      // TODO(sebright): Consider adding a TagValue class to avoid validating the String every time
      // it is set.
      checkArgument(StringUtil.isValid(value));
      return setInternal(key, value);
    }

    /**
     * Adds the key/value pair regardless of whether the key is present.
     *
     * @param key the {@code TagKey} which will be set.
     * @param value the value to set for the given key.
     * @return this
     * @throws IllegalArgumentException if the key is null or the key is the wrong type.
     */
    // TODO(sebright): Make this public once we support types other than String.
    Builder set(TagKey<Long> key, long value) {
      checkArgument(key.getTagType() == TagType.TAG_LONG);
      return setInternal(key, value);
    }

    /**
     * Adds the key/value pair regardless of whether the key is present.
     *
     * @param key the {@code TagKey} which will be set.
     * @param value the value to set for the given key.
     * @return this
     * @throws IllegalArgumentException if the key is null or the key is the wrong type.
     */
    // TODO(sebright): Make this public once we support types other than String.
    Builder set(TagKey<Boolean> key, boolean value) {
      checkArgument(key.getTagType() == TagType.TAG_BOOLEAN);
      return setInternal(key, value);
    }

    private <TagValueT> Builder setInternal(TagKey<TagValueT> key, TagValueT value) {
      tags.put(key, value);
      return this;
    }

    /**
     * Removes the key if it exists.
     *
     * @param key the {@code TagKey} which will be cleared.
     * @return this
     */
    public Builder clear(TagKey<?> key) {
      tags.remove(key);
      return this;
    }

    /**
     * Creates a {@code TagContext} from this builder.
     *
     * @return a {@code TagContext} with the same tags as this builder.
     */
    public TagContext build() {
      return new TagContext(new HashMap<TagKey<?>, Object>(tags));
    }
  }
}
