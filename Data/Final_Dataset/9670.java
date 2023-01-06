/*
package unit.com.bitdubai.fermat_dmp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.structure.BitcoinWalletBasicWallet;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import ErrorManager;
import com.bitdubai.fermat_ccp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.structure.BitcoinWalletBasicWallet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import unit.com.bitdubai.fermat_dmp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.structure.mocks.MockErrorManager;
import unit.com.bitdubai.fermat_dmp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.structure.mocks.MockWalletIdsFile;

*/
/**
 * Created by jorgegonzalez on 2015.07.10..
 *//*

@RunWith(MockitoJUnitRunner.class)
public class CreateTest {

    @Mock
    private PluginFileSystem mockPluginFileSystem;
    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;
    @Mock
    private DatabaseFactory mockDatabaseFactory;
    @Mock
    private DatabaseTableFactory mockTableFactory;
    @Mock
    private Database mockDatabase;
    @Mock
    private DatabaseTable mockTable;
    @Mock
    private DatabaseTableRecord mockTableRecord;

    private ErrorManager mockErrorManager;

    private PluginTextFile mockWalletIdsFile;
    private CryptoWalletBasicWallet testBasicWallet;
    private UUID testBasicWalletId;

    @Before
    public void setUpBasicWallet(){
        mockErrorManager = new MockErrorManager();

        testBasicWalletId = UUID.randomUUID();
        testBasicWallet = new CryptoWalletBasicWallet(testBasicWalletId);
        testBasicWallet.setErrorManager(mockErrorManager);
        testBasicWallet.setPluginDatabaseSystem(mockPluginDatabaseSystem);
        testBasicWallet.setPluginFileSystem(mockPluginFileSystem);
    }

    @Before
    public void setUpWalletIdsFile(){
        mockWalletIdsFile = new MockWalletIdsFile();
    }

    @Ignore
    @Test
    public void Create_ValidWalletId_ReturnsInternalWalletId() throws Exception{

    }

}
*/
