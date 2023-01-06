package com.fabahaba.jedipus.pubsub;

import com.fabahaba.jedipus.client.SerializableConsumer;
import com.fabahaba.jedipus.client.SerializableFunction;
import com.fabahaba.jedipus.executor.RedisClientExecutor;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public interface RedisSubscriber extends Runnable {

  public static Builder startBuilding() {
    return new Builder();
  }

  long getSubCount();

  void onSubscribed(final String channel, final long subCount);

  void onUnsubscribed(final String channel, final long subCount);

  void onMsg(final String channel, final byte[] payload);

  void onPMsg(final String pattern, final String channel, final byte[] payload);

  default void subscribe(final String... channels) {
    subscribe(null, channels);
  }

  void subscribe(final MsgConsumer msgConsumer, final String... channels);

  default void subscribe(final Collection<String> channels) {
    subscribe(null, channels);
  }

  void subscribe(final MsgConsumer msgConsumer, final Collection<String> channels);

  default void psubscribe(final String... patterns) {
    psubscribe(null, patterns);
  }

  void psubscribe(final MsgConsumer msgConsumer, final String... patterns);

  default void psubscribe(final Collection<String> patterns) {
    psubscribe(null, patterns);
  }

  void psubscribe(final MsgConsumer msgConsumer, final Collection<String> patterns);

  void registerConsumer(final MsgConsumer msgConsumer, final String... channels);

  default void registerPConsumer(final MsgConsumer msgConsumer, final String... patterns) {
    registerConsumer(msgConsumer, patterns);
  }

  void unRegisterConsumer(final MsgConsumer msgConsumer, final String... channels);

  default void unRegisterPConsumer(final MsgConsumer msgConsumer, final String... patterns) {
    unRegisterConsumer(msgConsumer, patterns);
  }

  void registerConsumer(final MsgConsumer msgConsumer, final Collection<String> channels);

  default void registerPConsumer(final MsgConsumer msgConsumer, final Collection<String> patterns) {
    registerConsumer(msgConsumer, patterns);
  }

  void unRegisterConsumer(final MsgConsumer msgConsumer, final Collection<String> channels);

  default void unRegisterPConsumer(final MsgConsumer msgConsumer,
      final Collection<String> patterns) {
    unRegisterConsumer(msgConsumer, patterns);
  }

  void unsubscribe(final String... channels);

  void punsubscribe(final String... patterns);

  void unsubscribe(final Collection<String> channels);

  void punsubscribe(final Collection<String> patterns);

  void ping();

  void ping(final String pong);

  void onPong(final String pong);

  void close();

  public static class Builder implements Serializable {

    private static final long serialVersionUID = 7465574872719472102L;

    private int soTimeoutMillis = 0; // silently block forever.
    private SerializableConsumer<RedisSubscriber> onSocketTimeout = RedisSubscriber::ping;
    private SerializableFunction<String, Collection<MsgConsumer>> consumerCollectionFactory =
        ch -> new HashSet<>();
    private SerializableConsumer<String> pongConsumer = pong -> {
    };

    private Builder() {
    }

    public RedisSubscriber createSingleSubscriber(final RedisClientExecutor clientExecutor,
        final MsgConsumer msgConsumer) {
      return new SingleSubscriber(clientExecutor, soTimeoutMillis, onSocketTimeout, msgConsumer,
          pongConsumer);
    }

    public RedisSubscriber create(final RedisClientExecutor clientExecutor) {
      return create(clientExecutor, new HashMap<>());
    }

    public RedisSubscriber create(final RedisClientExecutor clientExecutor,
        final MsgConsumer defaultConsumer) {
      return create(clientExecutor, new HashMap<>(), defaultConsumer);
    }

    public RedisSubscriber create(final RedisClientExecutor clientExecutor,
        final Map<String, MsgConsumer> msgConsumers) {
      return create(clientExecutor, msgConsumers, (ch, payload) -> {
      });
    }

    public RedisSubscriber create(final RedisClientExecutor clientExecutor,
        final Map<String, MsgConsumer> msgConsumers, final MsgConsumer defaultConsumer) {
      return new MappedSubscriber(clientExecutor, soTimeoutMillis, onSocketTimeout, defaultConsumer,
          msgConsumers, pongConsumer);
    }

    public RedisSubscriber createMulti(final RedisClientExecutor clientExecutor) {
      return createMulti(clientExecutor, new HashMap<>());
    }

    public RedisSubscriber createMulti(final RedisClientExecutor clientExecutor,
        final MsgConsumer defaultConsumer) {
      return createMulti(clientExecutor, new HashMap<>(), defaultConsumer);
    }

    public RedisSubscriber createMulti(final RedisClientExecutor clientExecutor,
        final Map<String, Collection<MsgConsumer>> msgConsumers) {
      return createMulti(clientExecutor, msgConsumers, (ch, payload) -> {
      });
    }

    public RedisSubscriber createMulti(final RedisClientExecutor clientExecutor,
        final Map<String, Collection<MsgConsumer>> msgConsumers,
        final MsgConsumer defaultConsumer) {
      return new MultiMappedSubscriber(clientExecutor, soTimeoutMillis, onSocketTimeout,
          defaultConsumer, msgConsumers, consumerCollectionFactory, pongConsumer);
    }

    public int getSoTimeoutMillis() {
      return soTimeoutMillis;
    }

    public Builder withSoTimeoutMillis(final int soTimeoutMillis) {
      this.soTimeoutMillis = soTimeoutMillis;
      return this;
    }

    public SerializableConsumer<RedisSubscriber> getOnSocketTimeout() {
      return onSocketTimeout;
    }

    public Builder withOnSocketTimeout(
        final SerializableConsumer<RedisSubscriber> onSocketTimeout) {
      this.onSocketTimeout = onSocketTimeout;
      return this;
    }

    public SerializableFunction<String, Collection<MsgConsumer>> getConsumerCollectionFactory() {
      return consumerCollectionFactory;
    }

    public Builder withConsumerCollectionFactory(
        final SerializableFunction<String, Collection<MsgConsumer>> consumerCollectionFactory) {
      this.consumerCollectionFactory = consumerCollectionFactory;
      return this;
    }

    public SerializableConsumer<String> getPongConsumer() {
      return pongConsumer;
    }

    public Builder withPongConsumer(final SerializableConsumer<String> pongConsumer) {
      this.pongConsumer = pongConsumer;
      return this;
    }
  }
}
