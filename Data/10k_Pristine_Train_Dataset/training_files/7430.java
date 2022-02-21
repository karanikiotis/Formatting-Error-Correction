package org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.database;

/**
 * Created by franklin on 25/09/15.
 */
public class AssetWalletIssuerDatabaseConstant {
    /**
     * Asset Wallet Issuer database table definition.
     */
    public static final String ASSET_WALLET_ISSUER_TABLE_NAME = "AssetWalletIssuer";
    public static final String ASSET_WALLET_ISSUER_TABLE_ID_COLUMN_NAME = "Id";
    // This second Id is used to verify that that the same transaction is not applied twice.
    // We can't use the transaction hash because some credit/debit operations do not involve a hash
    public static final String ASSET_WALLET_ISSUER_VERIFICATION_ID_COLUMN_NAME = "VerificationId";
    public static final String ASSET_WALLET_ISSUER_ASSET_PUBLIC_KEY_COLUMN_NAME = "assetPublicKey";
    public static final String ASSET_WALLET_ISSUER_ADDRESS_FROM_COLUMN_NAME = "addressFrom";
    public static final String ASSET_WALLET_ISSUER_ADDRESS_TO_COLUMN_NAME = "addressTo";
    public static final String ASSET_WALLET_ISSUER_ACTOR_FROM_COLUMN_NAME = "actorFrom";
    public static final String ASSET_WALLET_ISSUER_ACTOR_TO_COLUMN_NAME = "actorTo";
    public static final String ASSET_WALLET_ISSUER_ACTOR_FROM_TYPE_COLUMN_NAME = "actorFromType";
    public static final String ASSET_WALLET_ISSUER_ACTOR_TO_TYPE_COLUMN_NAME = "actorToType";
    public static final String ASSET_WALLET_ISSUER_AMOUNT_COLUMN_NAME = "amount";
    public static final String ASSET_WALLET_ISSUER_TYPE_COLUMN_NAME = "type";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TYPE_COLUMN_NAME = "balanceType";
    public static final String ASSET_WALLET_ISSUER_TIME_STAMP_COLUMN_NAME = "timestamp";
    public static final String ASSET_WALLET_ISSUER_MEMO_COLUMN_NAME = "memo";
    public static final String ASSET_WALLET_ISSUER_TRANSACTION_HASH_COLUMN_NAME = "transactionHash";
    public static final String ASSET_WALLET_ISSUER_RUNNING_BOOK_BALANCE_COLUMN_NAME = "runningBookBalance";
    public static final String ASSET_WALLET_ISSUER_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME = "runningAvailableBalance";


    // tabla nueva movimientos del resumen de los Assets - balance y book balance, id

    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_NAME = "AssetWalletIssuerTotalBalances";
    //public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_ID_COLUMN_NAME = "Id";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_ASSET_PUBLIC_KEY_COLUMN_NAME = "assetPublicKey";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_NAME_COLUMN_NAME = "name";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_DESCRIPTION_COLUMN_NAME = "description";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME = "availableBalance";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME = "bookBalance";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_QUANTITY_AVAILABLE_BALANCE_COLUMN_NAME = "quantityAvailableBalance";
    public static final String ASSET_WALLET_ISSUER_BALANCE_TABLE_QUANTITY_BOOK_BALANCE_COLUMN_NAME = "QuantityBookBalance";

    // ASSET STATISTIC TABLE.

    public static final String ASSET_STATISTIC_TABLE_NAME = "asset_statistics";
    public static final String ASSET_STATISTIC_TRANSACTION_ID_COLUMN_NAME = "transaction_id";
    public static final String ASSET_STATISTIC_TRANSACTION_HASH_COLUMN_NAME = "transactionHash";
    public static final String ASSET_STATISTIC_ASSET_PUBLIC_KEY_COLUMN_NAME = "asset_public_key";
    public static final String ASSET_STATISTIC_ACTOR_USER_PUBLIC_KEY_COLUMN_NAME = "user_public_key";
    public static final String ASSET_STATISTIC_REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME = "redeem_point_public_key";
    public static final String ASSET_STATISTIC_DISTRIBUTION_DATE_COLUMN_NAME = "distribution_date";
    public static final String ASSET_STATISTIC_ASSET_USAGE_DATE_COLUMN_NAME = "asset_usage_date";
    public static final String ASSET_STATISTIC_ASSET_CURRENT_STATUS_COLUMN_NAME = "asset_current_status";
    public static final String ASSET_STATISTIC_ASSET_NAME_COLUMN_NAME = "asset_name";

    // ASSET MOVEMENTS TABLE
    public static final String ASSET_MOVEMENTS_TABLE_NAME = "asset_movements";
    public static final String ASSET_MOVEMENTS_ENTRY_ID = "uniqueId";
    public static final String ASSET_MOVEMENTS_ASSET_PUBLIC_KEY = "asset_public_key";
    public static final String ASSET_MOVEMENTS_ACTOR_FROM_PUBLIC_KEY = "actorFromPublicKey";
    public static final String ASSET_MOVEMENTS_ACTOR_FROM_TYPE = "actorFromType";
    public static final String ASSET_MOVEMENTS_ACTOR_TO_PUBLIC_KEY = "actorToPublicKey";
    public static final String ASSET_MOVEMENTS_ACTOR_TO_TYPE = "actorToType";
    public static final String ASSET_MOVEMENTS_TIMESTAMP = "timestamp";
    public static final String ASSET_MOVEMENTS_TYPE = "movement_type";

}
