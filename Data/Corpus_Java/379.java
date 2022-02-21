package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events;

import com.bitdubai.fermat_api.layer.all_definition.events.common.AbstractEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.base.STATUS;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientNewMessageDeliveredEvent</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 17/05/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkClientNewMessageDeliveredEvent extends AbstractEvent<P2pEventType> {

    /**
     * Represent the content value
     */
    private String id;
    private STATUS status;

    /**
     * Constructor with parameters
     *
     * @param p2pEventType
     */
    public NetworkClientNewMessageDeliveredEvent(P2pEventType p2pEventType) {
        super(p2pEventType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NetworkClientNewMessageDeliveredEvent{" +
                "id='" + id + '\'' +
                ", status=" + status +
                '}';
    }
}
