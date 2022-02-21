package org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by root on 06/10/15.
 */
public class CantRegisterActorAssetUserException extends DAPException {


    public static final String DEFAULT_MESSAGE = "CAN'T REGISTER NEW USER";

    public CantRegisterActorAssetUserException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantRegisterActorAssetUserException(final Exception cause, final String context, final String possibleReason) {
        super(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    public CantRegisterActorAssetUserException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantRegisterActorAssetUserException(final String message) {
        this(message, null);
    }

    public CantRegisterActorAssetUserException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    public CantRegisterActorAssetUserException() {
        this(DEFAULT_MESSAGE);
    }
}
