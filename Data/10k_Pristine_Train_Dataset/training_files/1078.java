package org.fermat.fermat_dap_api.layer.dap_actor_network_service.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by natalia on 05/10/15.
 */
public class CantGetActorAssetNotificationException extends FermatException {

    private static final String DEFAULT_MESSAGE = "ACTOR ASSET REQUEST NOT FOUND EXCEPTION";

    /**
     * This is the constructor that every inherited FermatException must implement
     *
     * @param message        the short description of the why this exception happened, there is a public static constant called DEFAULT_MESSAGE that can be used here
     * @param cause          the exception that triggered the throwing of the current exception, if there are no other exceptions to be declared here, the cause should be null
     * @param context        a String that provides the values of the variables that could have affected the exception
     * @param possibleReason an explicative reason of why we believe this exception was most likely thrown
     */
    public CantGetActorAssetNotificationException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantGetActorAssetNotificationException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    public CantGetActorAssetNotificationException(String context, String possibleReason) {
        this(DEFAULT_MESSAGE, null, context, possibleReason);
    }
}
