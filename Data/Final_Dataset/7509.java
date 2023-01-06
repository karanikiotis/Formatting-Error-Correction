/*
package unit.com.bitdubai.fermat_ccp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.WalletContactsMiddlewareRegistry;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantCreateWalletContactException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_ccp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.database.WalletContactsMiddlewareDao;
import com.bitdubai.fermat_ccp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.WalletContactsMiddlewareRegistry;
import ErrorManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

*/
/**
 * Created by natalia on 11/09/15.
 *//*


@RunWith(PowerMockRunner.class)
@PrepareForTest({CryptoCurrency.class,Actors.class})
public class CreateWalletContactTest {
    private WalletContactsMiddlewareDao walletContactsMiddlewareDao;

    private WalletContactsMiddlewareRegistry walletContactsMiddlewareRegistry;

    @Mock
    private Database mockDatabase;

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;

    @Mock
    private DatabaseFactory mockDatabaseFactory;

    @Mock
    private DatabaseTable mockTable;

    @Mock
    private DatabaseTableRecord mockTableRecord;

    @Mock
    private List<DatabaseTableRecord> mockTableRecordList;

    @Mock
    private DatabaseTableFactory mockTableFactory;

    @Mock
    private CryptoAddress mockCryptoAddress;

    @Mock
    private ErrorManager mockErrorManager;

    @Mock
    private LogManager mockLogManager;

    private CryptoCurrency mockCryptoCurrency;

    private Actors mockActors;

    private UUID testOwnerId1;

    @Mock
    private List<CryptoAddress> cryptoAddressesList;



    @Before
    public void setUp() throws Exception {
        testOwnerId1 = UUID.randomUUID();
        mockActors = Actors.INTRA_USER;
        mockCryptoCurrency = CryptoCurrency.BITCOIN;
        cryptoAddressesList = Arrays.asList(mockCryptoAddress, mockCryptoAddress);


        Mockito.when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);
        Mockito.when(mockDatabase.getTable(anyString())).thenReturn(mockTable);
        Mockito.when(mockTable.getEmptyRecord()).thenReturn(mockTableRecord);
        Mockito.when(mockCryptoAddress.getCryptoCurrency()).thenReturn(mockCryptoCurrency);
        mockStatic(Actors.class);

        mockStatic(CryptoCurrency.class);

        PowerMockito.when(mockCryptoCurrency.getByCode(anyString())).thenReturn(CryptoCurrency.BITCOIN);
        walletContactsMiddlewareDao = new WalletContactsMiddlewareDao(mockPluginDatabaseSystem, testOwnerId1);
        walletContactsMiddlewareDao.initialize();

        walletContactsMiddlewareRegistry = new WalletContactsMiddlewareRegistry(mockErrorManager,mockLogManager,mockPluginDatabaseSystem,UUID.randomUUID());

        walletContactsMiddlewareRegistry.initialize();
    }

    @Test
    public void createWalletContactTest_CreateOK_ThrowsCantCreateWalletContactException() throws Exception {

        catchException(walletContactsMiddlewareRegistry).createWalletContact(UUID.randomUUID().toString(),
                "actorAlias",
                "actorFirstName",
                "actorLastName",
                mockActors,
                cryptoAddressesList,
                UUID.randomUUID().toString());

        assertThat(caughtException())
                .isNull();

    }


    @Test
    public void createWalletContactTest_listError_ThrowsCantCreateWalletContactException() throws Exception {

        Mockito.when(mockTable.getEmptyRecord()).thenReturn(null);
        catchException(walletContactsMiddlewareRegistry).createWalletContact(UUID.randomUUID().toString(),
                "actorAlias",
                "actorFirstName",
                "actorLastName",
                mockActors,
                cryptoAddressesList,
                UUID.randomUUID().toString());

        assertThat(caughtException())
                .isNotNull().isInstanceOf(CantCreateWalletContactException.class);

    }


}


*/
