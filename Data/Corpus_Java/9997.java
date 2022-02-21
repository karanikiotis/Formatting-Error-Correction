package com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.enums.InputTransactionState;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.enums.InputTransactionType;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.InputTransaction;

import java.security.InvalidParameterException;
import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.InputTransaction</code>
 * contains all the information about an InputTransaction.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 09/02/2016.
 *
 * @author lnacosta
 * @version 1.0
 */
public final class MatchingEngineMiddlewareInputTransaction implements InputTransaction {

    private final UUID id;
    private final String originTransactionId;
    private final Currency currencyGiving;
    private final float amountGiving;
    private final Currency currencyReceiving;
    private final float amountReceiving;
    private final InputTransactionType type;
    private final InputTransactionState state;

    public MatchingEngineMiddlewareInputTransaction(final UUID id,
                                                    final String originTransactionId,
                                                    final Currency currencyGiving,
                                                    final float amountGiving,
                                                    final Currency currencyReceiving,
                                                    final float amountReceiving,
                                                    final InputTransactionType type,
                                                    final InputTransactionState state) {

        if (id == null)
            throw new InvalidParameterException("id is null.");

        if (originTransactionId == null)
            throw new InvalidParameterException("originTransactionId is null.");

        if (currencyGiving == null)
            throw new InvalidParameterException("currencyGiving is null.");

        if (currencyReceiving == null)
            throw new InvalidParameterException("currencyReceiving is null.");

        if (type == null)
            throw new InvalidParameterException("type is null.");

        if (state == null)
            throw new InvalidParameterException("state is null.");

        if (amountReceiving < 0)
            throw new InvalidParameterException("receiving amount can't be lower than 0");

        if (amountGiving < 0)
            throw new InvalidParameterException("giving amount can't be lower than 0");

        this.id = id;
        this.originTransactionId = originTransactionId;
        this.currencyGiving = currencyGiving;
        this.amountGiving = amountGiving;
        this.currencyReceiving = currencyReceiving;
        this.amountReceiving = amountReceiving;
        this.type = type;
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public String getOriginTransactionId() {
        return originTransactionId;
    }

    public Currency getCurrencyGiving() {
        return currencyGiving;
    }

    public float getAmountGiving() {
        return amountGiving;
    }

    public Currency getCurrencyReceiving() {
        return currencyReceiving;
    }

    public float getAmountReceiving() {
        return amountReceiving;
    }

    public InputTransactionType getType() {
        return type;
    }

    public InputTransactionState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "MatchingEngineMiddlewareInputTransaction{" + "id=" + id + ", originTransactionId='" + originTransactionId + '\'' + ", currencyGiving=" + currencyGiving + ", amountGiving=" + amountGiving + ", currencyReceiving=" + currencyReceiving + ", amountReceiving=" + amountReceiving + ", type=" + type + ", state=" + state + '}';
    }
}
