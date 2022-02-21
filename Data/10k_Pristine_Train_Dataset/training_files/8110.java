package com.bitdubai.fermat_ccm_plugin.layer.actor.intra_wallet_user.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by natalia on 18/08/15.
 */
public class CantProcessNotificationsExceptions extends FermatException {


    public CantProcessNotificationsExceptions(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }


}