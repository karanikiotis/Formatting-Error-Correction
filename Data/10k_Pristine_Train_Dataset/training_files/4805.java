package com.bitdubai.fermat_bch_api.layer.crypto_vault.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by rodrigo on 12/16/15.
 */
public class CantAddHierarchyAccountException extends FermatException {
    public static final String DEFAULT_MESSAGE = "There was an error adding a new Hierarchy Account.";
    public CantAddHierarchyAccountException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
