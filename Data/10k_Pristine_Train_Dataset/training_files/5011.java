package com.bitdubai.fermat_bch_plugin.layer.crypto_vault.fermat.developer.bitdubai.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by rodrigo on 12/17/15.
 */
public class CantValidateCryptoNetworkIsActiveException extends FermatException {

    public final static String DEFAULT_MESSAGE = "There was an error validating the network is arctive or not.";

    public CantValidateCryptoNetworkIsActiveException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
