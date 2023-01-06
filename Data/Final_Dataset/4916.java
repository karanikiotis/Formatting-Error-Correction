//package unit.com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.structure.OutgoingExtraUserTransactionManager;
//
//import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
//import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
//
//import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
//<<<<<<< HEAD:CCP/plugin/transaction/fermat-ccp-plugin-transaction-outgoing-extra-user-bitdubai/src/test/java/unit/com/bitdubai/fermat_dmp_plugin/layer/transaction/outgoing_extra_user/developer/bitdubai/version_1/structure/OutgoingExtraUserTransactionManager/sendTest.java
//import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.exceptions.CantCalculateBalanceException;
//import com.bitdubai.fermat_api.layer.dmp_basic_wallet.common.exceptions.CantLoadWalletException;
//=======
//import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantCalculateBalanceException;
//import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantLoadWalletException;
//>>>>>>> 193a4ce563d3916b505332563ad81fe262f074f2:DMP/plugin/transaction/fermat-dmp-plugin-transaction-outgoing-extra-user-bitdubai/src/test/java/unit/com/bitdubai/fermat_dmp_plugin/layer/transaction/outgoing_extra_user/developer/bitdubai/version_1/structure/OutgoingExtraUserTransactionManager/sendTest.java
//import CryptoWalletBalance;
//import CryptoWalletManager;
//import CryptoWalletWallet;
//
//import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_extra_user.exceptions.CantSendFundsException;
//import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing_extra_user.exceptions.InsufficientFundsException;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
//
//import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
//
//import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
//import ErrorManager;
//import EventManager;
//import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
//import com.bitdubai.fermat_ccp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.structure.OutgoingExtraUserDatabaseConstants;
//import com.bitdubai.fermat_ccp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.structure.OutgoingExtraUserTransactionManager;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.util.UUID;
//
//import static com.googlecode.catchexception.CatchException.catchException;
//import static com.googlecode.catchexception.CatchException.caughtException;
//import static org.fest.assertions.api.Assertions.assertThat;
//import static org.mockito.Matchers.any;
//
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.when;
//
///**
// * Created by natalia on 10/07/15.
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class sendTest {
//
//    @Mock
//    private CryptoWalletManager mockBitcoinWalletManager;
//
//    @Mock
//    private CryptoWalletWallet mockBitcoinWalletWallet;
//
//    @Mock
//    private CryptoWalletBalance mockBitcoinWalletBalance;
//
//    @Mock
//    private CryptoVaultManager mockCryptoVaultManager;
//    @Mock
//    private ErrorManager mockErrorManager;
//
//    @Mock
//    private PluginDatabaseSystem mockPluginDatabaseSystem;
//
//    @Mock
//    private EventManager mockEventManager;
//
//    @Mock
//    private DatabaseFactory mockDatabaseFactory;
//    @Mock
//    private DatabaseTableFactory mockTableFactory;
//    @Mock
//    private Database mockDatabase;
//
//    @Mock
//    private DatabaseTable mockTable;
//
//
//    @Mock
//    private DatabaseTableRecord mockRecord;
//
//    private CryptoAddress cryptoAddress;
//    private UUID testId;
//    private String testwalletID;
//    private String testDataBaseName;
//    private OutgoingExtraUserTransactionManager testTransactionManager;
//
//
//    public void setUpTestValues(){
//        cryptoAddress = new CryptoAddress();
//        cryptoAddress.setCryptoCurrency(CryptoCurrency.BITCOIN);
//        cryptoAddress.setAddress(UUID.randomUUID().toString());
//        testwalletID = "0450863AD64A87AE8A2FE83C1AF1A8403CB53F53E486D8511DAD8A04887E5B23522CD470243453A299FA9E77237716103ABC11A1DF38855ED6F2EE187E9C582BA6";
//        testId = UUID.randomUUID();
//        testDataBaseName = OutgoingExtraUserDatabaseConstants.OUTGOING_EXTRA_USER_DATABASE_NAME;
//
//
//    }
//
//    public void setUpGeneralMockitoRules() throws Exception{
//
//        when(mockPluginDatabaseSystem.createDatabase(testId, testDataBaseName)).thenReturn(mockDatabase);
//        when(mockPluginDatabaseSystem.openDatabase(testId, testDataBaseName)).thenReturn(mockDatabase);
//        when(mockDatabase.getDatabaseFactory()).thenReturn(mockDatabaseFactory);
//        when(mockDatabaseFactory.newTableFactory(any(UUID.class), anyString())).thenReturn(mockTableFactory);
//        when(mockBitcoinWalletManager.loadWallet(anyString())).thenReturn(mockBitcoinWalletWallet);
//        when(mockBitcoinWalletWallet.getAvailableBalance()).thenReturn(mockBitcoinWalletBalance);
//
//        when(mockDatabase.getTable(anyString())).thenReturn(mockTable);
//        when(mockTable.getEmptyRecord()).thenReturn(mockRecord);
//    }
//
//    @Before
//    public void setUp() throws Exception{
//        setUpTestValues();
//        setUpGeneralMockitoRules();
//    }
//
//
//
//    @Test
//    public void Send_Succesfully() throws Exception {
//
//        testTransactionManager = new OutgoingExtraUserTransactionManager();
//        testTransactionManager.setPluginDatabaseSystem(mockPluginDatabaseSystem);
//        testTransactionManager.setPluginId(testId);
//        testTransactionManager.setErrorManager(mockErrorManager);
//
//        testTransactionManager.setBitcoinWalletManager(mockBitcoinWalletManager);
//        testTransactionManager.setCryptoVaultManager(mockCryptoVaultManager);
//
//           catchException(testTransactionManager).send(testwalletID, cryptoAddress, 0, "test", UUID.randomUUID(), Actors.EXTRA_USER, UUID.randomUUID(), Actors.EXTRA_USER);
//
//        assertThat(caughtException()).isNull();
//
//
//    }
//
//    @Test
//    public void Send_InsufficientFunds_TrowsInsufficientFundsException() throws Exception {
//        testTransactionManager = new OutgoingExtraUserTransactionManager();
//        testTransactionManager.setPluginDatabaseSystem(mockPluginDatabaseSystem);
//        testTransactionManager.setPluginId(testId);
//        testTransactionManager.setErrorManager(mockErrorManager);
//
//
//        testTransactionManager.setBitcoinWalletManager(mockBitcoinWalletManager);
//        testTransactionManager.setCryptoVaultManager(mockCryptoVaultManager);
//
//        catchException(testTransactionManager).send(testwalletID, cryptoAddress, 20, "test", UUID.randomUUID(), Actors.EXTRA_USER, UUID.randomUUID(), Actors.EXTRA_USER);
//
//        assertThat(caughtException()).isInstanceOf(InsufficientFundsException.class);
//
//
//    }
//
//
//    @Test
//    public void Send_FailedCalculateBalance_ThrowsCantSendFundsException() throws Exception {
//        when(mockBitcoinWalletWallet.getAvailableBalance().getBalance()).thenThrow(new CantCalculateBalanceException("Mock", null, null, null));
//
//        testTransactionManager = new OutgoingExtraUserTransactionManager();
//        testTransactionManager.setPluginDatabaseSystem(mockPluginDatabaseSystem);
//        testTransactionManager.setPluginId(testId);
//        testTransactionManager.setErrorManager(mockErrorManager);
//        testTransactionManager.setBitcoinWalletManager(mockBitcoinWalletManager);
//        testTransactionManager.setCryptoVaultManager(mockCryptoVaultManager);
//
//        catchException(testTransactionManager).send(testwalletID, cryptoAddress, 0, "test", UUID.randomUUID(),Actors.EXTRA_USER,UUID.randomUUID(),Actors.EXTRA_USER);
//
//        assertThat(caughtException()).isInstanceOf(CantSendFundsException.class);
//    }
//
//    @Test
//    public void Send_FailedDaoInitialize_ThrowsCantSendFundsException() throws Exception {
//        when(mockPluginDatabaseSystem.openDatabase(testId, testDataBaseName)).thenThrow(new CantOpenDatabaseException());
//
//        testTransactionManager = new OutgoingExtraUserTransactionManager();
//        testTransactionManager.setPluginDatabaseSystem(mockPluginDatabaseSystem);
//        testTransactionManager.setPluginId(testId);
//        testTransactionManager.setErrorManager(mockErrorManager);
//        testTransactionManager.setBitcoinWalletManager(mockBitcoinWalletManager);
//        testTransactionManager.setCryptoVaultManager(mockCryptoVaultManager);
//
//        catchException(testTransactionManager).send(testwalletID, cryptoAddress, 0, "test", UUID.randomUUID(),Actors.EXTRA_USER,UUID.randomUUID(),Actors.EXTRA_USER);
//
//        assertThat(caughtException()).isInstanceOf(CantSendFundsException.class);
//    }
//
//
//
//    @Test
//    public void Send_FailedWalletLoad_ThrowsCantSendFundsException() throws Exception {
//        when(mockBitcoinWalletManager.loadWallet(anyString())).thenThrow(new CantLoadWalletException("Mock", null, null, null));
//
//        testTransactionManager = new OutgoingExtraUserTransactionManager();
//        testTransactionManager.setPluginDatabaseSystem(mockPluginDatabaseSystem);
//        testTransactionManager.setPluginId(testId);
//        testTransactionManager.setErrorManager(mockErrorManager);
//        testTransactionManager.setBitcoinWalletManager(mockBitcoinWalletManager);
//        testTransactionManager.setCryptoVaultManager(mockCryptoVaultManager);
//
//        catchException(testTransactionManager).send(testwalletID, cryptoAddress, 0, "test", UUID.randomUUID(),Actors.EXTRA_USER,UUID.randomUUID(),Actors.EXTRA_USER);
//
//        assertThat(caughtException()).isInstanceOf(CantSendFundsException.class);
//    }
//
//}
//
//
