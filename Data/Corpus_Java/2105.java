package com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * The exception <code>CantAcceptAddressExchangeRequestException</code>
 * is thrown when there is an error trying to accept an address exchange request.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 22/09/2015.
 */
public class CantAcceptAddressExchangeRequestException extends FermatException {

    private static final String DEFAULT_MESSAGE = "CAN'T ACCEPT ADDRESS EXCHANGE REQUEST EXCEPTION";

    public CantAcceptAddressExchangeRequestException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantAcceptAddressExchangeRequestException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

}
