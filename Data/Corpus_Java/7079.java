/**
 * Copyright (c) 2016, All Contributors (see CONTRIBUTORS file)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.eventsourcing.events;

import com.eventsourcing.StandardEvent;
import com.eventsourcing.hlc.HybridTimestamp;
import com.eventsourcing.index.SimpleIndex;
import com.eventsourcing.layout.LayoutName;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.unprotocols.coss.RFC;
import org.unprotocols.coss.Raw;

import java.util.UUID;

@Accessors(fluent = true)
@LayoutName("rfc.eventsourcing.com/spec:9/RIG/#EventCausalityEstablished")
@Raw @RFC(url = "http://rfc.eventsourcing.com/spec:9/RIG/", revision = "July 22, 2016")
public class EventCausalityEstablished extends StandardEvent {
    @Getter
    private final UUID event;
    @Getter
    private final UUID command;

    @Builder
    public EventCausalityEstablished(HybridTimestamp timestamp, UUID event, UUID command) {
        super(timestamp);
        this.event = event;
        this.command = command;
    }

    public final static SimpleIndex<EventCausalityEstablished, UUID> EVENT = SimpleIndex.as(EventCausalityEstablished::event);

    public final static SimpleIndex<EventCausalityEstablished, UUID> COMMAND = SimpleIndex.as(EventCausalityEstablished::command);
}
