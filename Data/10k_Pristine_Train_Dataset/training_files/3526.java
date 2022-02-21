package com.bitdubai.fermat_cbp_plugin.layer.wallet_module.crypto_customer.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractModule;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetModuleManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.ActorExtraDataManager;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_broker.interfaces.CryptoBrokerActorConnectionManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.broker_ack_offline_payment.interfaces.BrokerAckOfflinePaymentManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.broker_ack_online_payment.interfaces.BrokerAckOnlinePaymentManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.broker_submit_offline_merchandise.interfaces.BrokerSubmitOfflineMerchandiseManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.broker_submit_online_merchandise.interfaces.BrokerSubmitOnlineMerchandiseManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.customer_ack_offline_merchandise.interfaces.CustomerAckOfflineMerchandiseManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.customer_ack_online_merchandise.interfaces.CustomerAckOnlineMerchandiseManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.customer_offline_payment.interfaces.CustomerOfflinePaymentManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.customer_online_payment.interfaces.CustomerOnlinePaymentManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.interfaces.CryptoCustomerIdentityManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.interfaces.CustomerBrokerCloseManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_new.interfaces.CustomerBrokerNewManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_update.interfaces.CustomerBrokerUpdateManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletModuleManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.settings.CryptoCustomerWalletPreferenceSettings;
import com.bitdubai.fermat_cbp_plugin.layer.wallet_module.crypto_customer.developer.bitdubai.version_1.structure.CryptoCustomerWalletModuleCryptoCustomerWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces.IntraWalletUserIdentityManager;
import com.bitdubai.fermat_cer_api.layer.search.interfaces.CurrencyExchangeProviderFilterManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.WalletManagerManager;


/**
 * Module for the Crypto Customer Wallet
 *
 * @author Nelson Ramirez
 * @version 1.0
 * @since 11/11/2015
 */
@PluginInfo(createdBy = "nelsonalfo", maintainerMail = "nelsonalfo@gmail.com", platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.WALLET_MODULE, plugin = Plugins.CRYPTO_CUSTOMER)
public class CryptoCustomerWalletModulePluginRoot extends AbstractModule<CryptoCustomerWalletPreferenceSettings, ActiveActorIdentityInformation> {

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    PluginFileSystem pluginFileSystem;

    @NeededPluginReference(platform = Platforms.WALLET_PRODUCTION_AND_DISTRIBUTION, layer = Layers.MIDDLEWARE, plugin = Plugins.WALLET_MANAGER)
    WalletManagerManager walletManagerManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION, plugin = Plugins.NEGOTIATION_PURCHASE)
    CustomerBrokerPurchaseNegotiationManager customerBrokerPurchaseNegotiationManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.CRYPTO_CUSTOMER)
    CryptoCustomerIdentityManager cryptoCustomerIdentityManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.CONTRACT, plugin = Plugins.CONTRACT_PURCHASE)
    CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION_TRANSACTION, plugin = Plugins.CUSTOMER_BROKER_NEW)
    CustomerBrokerNewManager customerBrokerNewManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION_TRANSACTION, plugin = Plugins.CUSTOMER_BROKER_UPDATE)
    CustomerBrokerUpdateManager customerBrokerUpdateManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION_TRANSACTION, plugin = Plugins.CUSTOMER_BROKER_CLOSE)
    CustomerBrokerCloseManager customerBrokerCloseManager;

    @NeededPluginReference(platform = Platforms.CURRENCY_EXCHANGE_RATE_PLATFORM, layer = Layers.SEARCH, plugin = Plugins.FILTER)
    CurrencyExchangeProviderFilterManager currencyExchangeProviderFilterManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.ACTOR, plugin = Plugins.CRYPTO_CUSTOMER_ACTOR)
    ActorExtraDataManager actorExtraDataManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.CUSTOMER_ONLINE_PAYMENT)
    CustomerOnlinePaymentManager customerOnlinePaymentManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.CUSTOMER_OFFLINE_PAYMENT)
    CustomerOfflinePaymentManager customerOfflinePaymentManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.CUSTOMER_ACK_ONLINE_MERCHANDISE)
    CustomerAckOnlineMerchandiseManager customerAckOnlineMerchandiseManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.CUSTOMER_ACK_OFFLINE_MERCHANDISE)
    CustomerAckOfflineMerchandiseManager customerAckOfflineMerchandiseManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.BROKER_ACK_ONLINE_PAYMENT)
    BrokerAckOnlinePaymentManager brokerAckOnlinePaymentManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.BROKER_ACK_OFFLINE_PAYMENT)
    BrokerAckOfflinePaymentManager brokerAckOfflinePaymentManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.BROKER_SUBMIT_ONLINE_MERCHANDISE)
    BrokerSubmitOnlineMerchandiseManager brokerSubmitOnlineMerchandiseManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.BROKER_SUBMIT_OFFLINE_MERCHANDISE)
    BrokerSubmitOfflineMerchandiseManager brokerSubmitOfflineMerchandiseManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.ACTOR_CONNECTION, plugin = Plugins.CRYPTO_BROKER)
    CryptoBrokerActorConnectionManager cryptoBrokerActorConnectionManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.BASIC_WALLET, plugin = Plugins.BITCOIN_WALLET)
    CryptoWalletManager cryptoWalletManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.INTRA_WALLET_USER)
    IntraWalletUserIdentityManager intraWalletUserIdentityManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.DEVICE_LOCATION)
    private LocationManager locationManager;

    private CryptoCustomerWalletModuleManager walletManager;

    public CryptoCustomerWalletModulePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    @Override
    public void start() throws CantStartPluginException {
        super.start();
    }

    @Override
    public CryptoCustomerWalletModuleManager getModuleManager() throws CantGetModuleManagerException {
        if (walletManager == null)
            walletManager = new CryptoCustomerWalletModuleCryptoCustomerWalletManager(walletManagerManager,
                    cryptoBrokerActorConnectionManager,
                    customerBrokerPurchaseNegotiationManager,
                    cryptoCustomerIdentityManager,
                    customerBrokerContractPurchaseManager,
                    customerBrokerNewManager,
                    customerBrokerUpdateManager,
                    customerBrokerCloseManager,
                    currencyExchangeProviderFilterManager,
                    actorExtraDataManager,
                    customerOnlinePaymentManager,
                    customerOfflinePaymentManager,
                    customerAckOnlineMerchandiseManager,
                    customerAckOfflineMerchandiseManager,
                    brokerAckOnlinePaymentManager,
                    brokerAckOfflinePaymentManager,
                    brokerSubmitOnlineMerchandiseManager,
                    brokerSubmitOfflineMerchandiseManager,
                    intraWalletUserIdentityManager,
                    cryptoWalletManager,
                    this,
                    pluginFileSystem,
                    pluginId,
                    locationManager
            );

        return walletManager;
    }
}