//package unit.com.bitdubai.fermat_dmp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.CryptoWalletWalletModulePluginRoot;
//
//import CryptoWalletManager;
//<<<<<<< HEAD:CCP/plugin/wallet_module/fermat-ccp-plugin-wallet-module-crypto-wallet-bitdubai/src/test/java/unit/com/bitdubai/fermat_dmp_plugin/layer/wallet_module/crypto_wallet/developer/bitdubai/version_1/CryptoWalletWalletModulePluginRoot/ServiceInterfaceImplementationTest.java
//import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_extra_user.OutgoingExtraUserManager;
//=======
//import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.OutgoingExtraUserManager;
//>>>>>>> 193a4ce563d3916b505332563ad81fe262f074f2:CCP/plugin/wallet_module/fermat-ccp-plugin-wallet-module-crypto-wallet-bitdubai/src/test/java/unit/com/bitdubai/fermat_dmp_plugin/layer/wallet_module/crypto_wallet/developer/bitdubai/version_1/CryptoWalletWalletModulePluginRoot/ServiceInterfaceImplementationTest.java
//import com.bitdubai.fermat_ccp_api.layer.middleware.wallet_contacts.interfaces.WalletContactsManager;
//import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
//import com.bitdubai.fermat_ccp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.CryptoWalletCryptoModulePluginRoot;
//import ErrorManager;
//import com.bitdubai.fermat_ccp_api.layer.actor.extra_user.interfaces.ExtraUserManager;
//import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
//
//import junit.framework.TestCase;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.util.UUID;
//
//
//@RunWith(MockitoJUnitRunner.class)
//public class ServiceInterfaceImplementationTest extends TestCase {
//
//    /**
//     * DealsWithBitcoinWallet interface Mocked
//     */
//    @Mock
//    CryptoWalletManager cryptoWalletManager;
//
//    /**
//     * DealsWithCryptoVault interface Mocked
//     */
//    @Mock
//    CryptoVaultManager cryptoVaultManager;
//
//    /**
//     * DealsWithErrors interface Mocked
//     */
//    @Mock
//    ErrorManager errorManager;
//
//    /**
//     * DealsWithExtraUsers interface Mocked
//     */
//    @Mock
//    ExtraUserManager extraUserManager;
//
//    /**
//     * DealsWithOutgoingExtraUser interface Mocked
//     */
//    @Mock
//    OutgoingExtraUserManager outgoingExtraUserManager;
//
//    /**
//     * DealsWithCryptoAddressBook interface Mocked
//     */
//    @Mock
//    CryptoAddressBookManager cryptoAddressBookManager;
//
//    /**
//     * DealsWithWalletContacts interface Mocked
//     */
//    @Mock
//    WalletContactsManager walletContactsManager;
//
//
//    CryptoWalletCryptoModulePluginRoot cryptoWalletWalletModulePluginRoot;
//
//    @Before
//    public void setUp() throws Exception {
//        cryptoWalletWalletModulePluginRoot = new CryptoWalletCryptoModulePluginRoot();
//        cryptoWalletWalletModulePluginRoot.setBitcoinWalletManager(cryptoWalletManager);
//        cryptoWalletWalletModulePluginRoot.setCryptoVaultManager(cryptoVaultManager);
//        cryptoWalletWalletModulePluginRoot.setErrorManager(errorManager);
//        cryptoWalletWalletModulePluginRoot.setExtraUserManager(extraUserManager);
//        cryptoWalletWalletModulePluginRoot.setOutgoingExtraUserManager(outgoingExtraUserManager);
//        cryptoWalletWalletModulePluginRoot.setCryptoAddressBookManager(cryptoAddressBookManager);
//        cryptoWalletWalletModulePluginRoot.setWalletContactsManager(walletContactsManager);
//    }
//
//    @Test
//    public void testSetId() throws Exception {
//        cryptoWalletWalletModulePluginRoot.setId(UUID.randomUUID());
//    }
//}
