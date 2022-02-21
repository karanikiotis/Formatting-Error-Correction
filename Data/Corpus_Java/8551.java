package io.mewbase.common;

import io.mewbase.bson.BsonObject;

/**
 * Created by tim on 22/09/16.
 */
public interface Delivery {

    // Metadata for event

    String channel();

    long timeStamp();

    long channelPos();

    BsonObject event();


}
