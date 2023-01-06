package org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by franklin on 08/09/15.
 */
public class CantSaveAssetFactoryException extends FermatException {
    /**
     * This is the constructor that every inherited FermatException must implement
     *
     * @param message        the short description of the why this exception happened, there is a public static constant called DEFAULT_MESSAGE that can be used here
     * @param cause          the exception that triggered the throwing of the current exception, if there are no other exceptions to be declared here, the cause should be null
     * @param context        a String that provides the values of the variables that could have affected the exception
     * @param possibleReason an explicative reason of why we believe this exception was most likely thrown
     */
    static final String DEFAULT_MESSAGE = "There was an error Save Asset Factory.";

    public CantSaveAssetFactoryException(Exception cause, String context, String possibleReason) {
        super(DEFAULT_MESSAGE, cause, context, possibleReason);
    }
}
