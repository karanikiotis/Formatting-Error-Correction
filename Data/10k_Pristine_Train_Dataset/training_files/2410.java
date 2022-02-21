package com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;


/**
 * Created by rodrigo on 9/19/15.
 */
public interface BitcoinNetworkConfiguration {
    static final BlockchainNetworkType DEFAULT_NETWORK_TYPE = BlockchainNetworkType.getDefaultBlockchainNetworkType();
    static final CryptoCurrency CRYPTO_CURRENCY = CryptoCurrency.BITCOIN;
    static final int IRREVERSIBLE_BLOCK_DEPTH = 3;
    static final int FIXED_FEE_VALUE = 30000;
    static final int MIN_ALLOWED_SATOSHIS_ON_SEND = 5000;



}
