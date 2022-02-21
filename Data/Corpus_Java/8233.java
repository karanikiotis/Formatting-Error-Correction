/**
 * Copyright (c) 2016, All Contributors (see CONTRIBUTORS file)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.eventsourcing.queries;

import com.eventsourcing.Model;
import com.eventsourcing.Repository;

import java.util.*;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A toolkit for composing model collections queries. An implementation of this interface
 * can be passed to {@link #query(Repository, ModelCollectionQuery)} to retrieve a collection
 * of matching model instances.
 *
 * Following logical operator functions can be used to compose a query:
 *
 * <ul>
 *     <li>{@link LogicalOperators#or(Collection)}</li>
 *     <li>{@link LogicalOperators#and(Collection)}</li>
 * </ul>
 *
 * @param <T>
 */
public interface ModelCollectionQuery<T extends Model> {

    Stream<T> getCollectionStream(Repository repository);

    static <T extends Model> Collection<T> query(Repository repository, ModelCollectionQuery<T> query) {
        try (Stream<T> collectionStream = query.getCollectionStream(repository)) {
            return collectionStream.collect(Collectors.toList());
        }
    }


    final class LogicalOperators {

        public static <T extends Model> ModelCollectionQuery<T> or(Collection<ModelCollectionQuery<T>> queries) {
            return new Or<>(queries);
        }

        public static <T extends Model> ModelCollectionQuery<T> or(ModelCollectionQuery<T>... queries) {
            return new Or<>(queries);
        }

        public static class Or<T extends Model> implements ModelCollectionQuery<T> {

            private final Collection<ModelCollectionQuery<T>> queries;

            public Or(Collection<ModelCollectionQuery<T>> queries) {
                this.queries = queries;
            }

            public Or(ModelCollectionQuery<T>... queries) {
                this.queries = Arrays.asList(queries);
            }

            @Override public Stream<T> getCollectionStream(Repository repository) {
                Stream<T> stream = Stream.empty();
                List<Stream<T>> streams = new ArrayList<>();
                Set<UUID> seen = new HashSet<>();
                for (ModelCollectionQuery<T> query : queries) {
                    Stream<T> queryStream = query.getCollectionStream(repository)
                                                 .filter(m -> !seen.contains(m.getId()))
                                                 .map(m -> {
                                                     seen.add(m.getId());
                                                     return m;
                                                 });

                    streams.add(queryStream);
                    stream = Stream.concat(stream, queryStream);
                }
                return stream.onClose(() -> {
                    streams.forEach(BaseStream::close);
                });
            }
        }

        public static <T extends Model> ModelCollectionQuery<T> and(Collection<ModelCollectionQuery<T>> queries) {
            return new And<>(queries);
        }

        public static <T extends Model> ModelCollectionQuery<T> and(ModelCollectionQuery<T>... queries) {
            return new And<>(queries);
        }

        public static class And<T extends Model> implements ModelCollectionQuery<T> {

            private final Collection<ModelCollectionQuery<T>> queries;

            public And(Collection<ModelCollectionQuery<T>> queries) {
                this.queries = queries;
            }

            public And(ModelCollectionQuery<T>... queries) {
                this.queries = Arrays.asList(queries);
            }

            @Override public Stream<T> getCollectionStream(Repository repository) {
                Iterator<ModelCollectionQuery<T>> iterator = queries.iterator();
                if (iterator.hasNext()) {
                    ModelCollectionQuery<T> first = iterator.next();
                    Stream<T> firstStream = first.getCollectionStream(repository);
                    List<T> realizedStream = firstStream.collect(Collectors.toList());
                    Set<UUID> set = realizedStream.stream()
                                                  .map(Model::getId).collect(Collectors.toSet());
                    Set<UUID> seen = new HashSet<>();
                    firstStream.close();
                    List<ModelCollectionQuery<T>> allQueries = new ArrayList<>();
                    iterator.forEachRemaining(allQueries::add);

                    Stream<T> stream = Stream.empty();
                    List<Stream<T>> streams = new ArrayList<>();
                    for (ModelCollectionQuery<T> query : allQueries) {
                        Stream<T> queryStream =
                                query.getCollectionStream(repository)
                                     .filter(m -> set.contains(m.getId()) && !seen.contains(m.getId()))
                                     .map(m -> {
                                         seen.add(m.getId());
                                         return m;
                                     });
                        streams.add(queryStream);
                        stream = Stream.concat(stream, queryStream);
                    }
                    stream = Stream.concat(realizedStream.stream().filter(m -> seen.contains(m.getId())),
                                           stream);
                    return stream.onClose(() -> streams.forEach(BaseStream::close));

                } else {
                    return Stream.empty();
                }
            }
        }
    }
}
