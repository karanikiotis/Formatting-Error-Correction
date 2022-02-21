/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bson.codecs;

import org.bson.BsonReader;

/**
 * The context for decoding values to BSON.
 *
 * @see org.bson.codecs.Decoder
 * @since 3.0
 */
public final class DecoderContext {
    private static final DecoderContext DEFAULT_CONTEXT = DecoderContext.builder().build();
    private final boolean checkedDiscriminator;

    /**
     * @return true if the discriminator has been checked
     */
    public boolean hasCheckedDiscriminator() {
        return checkedDiscriminator;
    }

    /**
     * Create a builder.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@code DecoderContext} instances.
     */
    public static final class Builder {
        private Builder() {
        }

        private boolean checkedDiscriminator;

        /**
         * @return true if the discriminator has been checked
         */
        public boolean hasCheckedDiscriminator() {
            return checkedDiscriminator;
        }

        /**
         * Sets the checkedDiscriminator
         *
         * @param checkedDiscriminator the checkedDiscriminator
         * @return this
         */
        public Builder checkedDiscriminator(final boolean checkedDiscriminator) {
            this.checkedDiscriminator = checkedDiscriminator;
            return this;
        }

        /**
         * Build an instance of {@code DecoderContext}.
         * @return the decoder context
         */
        public DecoderContext build() {
            return new DecoderContext(this);
        }
    }

    /**
     * Creates a child context and then deserializes using the reader.
     *
     * @param decoder the decoder to decode with
     * @param reader the reader to decode to
     * @param <T> the type of the decoder
     * @return the decoded value
     * @since 3.5
     */
    public <T> T decodeWithChildContext(final Decoder<T> decoder, final BsonReader reader) {
        return decoder.decode(reader, DEFAULT_CONTEXT);
    }

    private DecoderContext(final Builder builder) {
        this.checkedDiscriminator = builder.hasCheckedDiscriminator();
    }
}
