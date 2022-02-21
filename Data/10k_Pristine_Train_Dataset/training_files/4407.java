package org.fermat.fermat_dap_api.layer.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 08/09/15.
 */
public enum TransactionStatus {

    CRYPTO_SENT("RXC"),
    DELIVERED("DELD"),
    DELIVERING("DELG"),
    FORMING_GENESIS("FGEN"),
    GENESIS_OBTAINED("OGEN"),
    GENESIS_SETTLED("SGEN"),
    HASH_SETTLED("SHASH"),
    ISSUED("ISSUED"),
    ISSUED_FAILED("FISSUED"),
    ISSUING("ISSUING"),
    RECEIVED("RX"),
    SENDING_CRYPTO("TXBTC"),
    SENDING_CRYPTO_FAILED("FTXBTC"),
    TO_DELIVER("TDEL");

    private String code;

    TransactionStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static TransactionStatus getByCode(String code) throws InvalidParameterException {
        switch (code) {
            case "RXC":
                return TransactionStatus.CRYPTO_SENT;
            case "DELD":
                return TransactionStatus.DELIVERED;
            case "DELG":
                return TransactionStatus.DELIVERING;
            case "FGEN":
                return TransactionStatus.FORMING_GENESIS;
            case "OGEN":
                return TransactionStatus.GENESIS_OBTAINED;
            case "SGEN":
                return TransactionStatus.GENESIS_SETTLED;
            case "SHASH":
                return TransactionStatus.HASH_SETTLED;
            case "ISSUED":
                return TransactionStatus.ISSUED;
            case "FISSUED":
                return TransactionStatus.ISSUED_FAILED;
            case "ISSUING":
                return TransactionStatus.ISSUING;
            case "RX":
                return TransactionStatus.RECEIVED;
            case "TXBTC":
                return TransactionStatus.SENDING_CRYPTO;
            case "FTXBTC":
                return TransactionStatus.SENDING_CRYPTO_FAILED;
            case "TDEL":
                return TransactionStatus.TO_DELIVER;
            default:
                throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + code, "This Code Is Not Valid for the TransactionStatus enum.");
        }
    }
}
