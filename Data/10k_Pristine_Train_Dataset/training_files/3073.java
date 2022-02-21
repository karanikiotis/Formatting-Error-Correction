package unit.com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.database.NetworkserviceswalletresourcesDatabaseFactory;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.database.NetworkserviceswalletresourcesDatabaseConstants;
import com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.database.NetworkserviceswalletresourcesDatabaseFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by natalia on 09/09/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class CreateDatabaseTest {

    private NetworkserviceswalletresourcesDatabaseFactory databaseFactory;

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;
    @Mock
    private Database mockDatabase;
    @Mock
    private DatabaseFactory mockDatabaseFactory;
    @Mock
    private DatabaseTable mockTable;


    @Mock
    private DatabaseTableFactory mockTableFactory;

    private UUID testOwnerId;

    @Before
    public void setUp() throws Exception {
        testOwnerId = UUID.randomUUID();

        when(mockPluginDatabaseSystem.createDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);
        when(mockDatabase.getDatabaseFactory()).thenReturn(mockDatabaseFactory);
        when(mockDatabaseFactory.newTableFactory(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.REPOSITORIES_TABLE_NAME)).thenReturn(mockTableFactory);
    }

    @Test
    public void IntraUserIdentityDatabaseFactoryTestConstructor() throws CantOpenDatabaseException, DatabaseNotFoundException, CantCreateDatabaseException {

        databaseFactory = new NetworkserviceswalletresourcesDatabaseFactory(mockPluginDatabaseSystem);
        databaseFactory.setPluginDatabaseSystem(mockPluginDatabaseSystem);

    }


    @Test
    public void CreateDatabase_DatabaseAndTablesProperlyCreated_ReturnsDatabase() throws Exception {
        databaseFactory = new NetworkserviceswalletresourcesDatabaseFactory(mockPluginDatabaseSystem);
        Database checkDatabase = databaseFactory.createDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.REPOSITORIES_TABLE_NAME);

        assertThat(checkDatabase).isEqualTo(mockDatabase);
    }

    @Test
    public void CreateDatabase_PluginSystemCantCreateDatabase_ThrowsCantCreateDatabaseException() throws Exception {
        when(mockPluginDatabaseSystem.createDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.DATABASE_NAME)).thenThrow(new CantCreateDatabaseException("MOCK", null, null, null));

        databaseFactory = new NetworkserviceswalletresourcesDatabaseFactory(mockPluginDatabaseSystem);
        catchException(databaseFactory).createDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.DATABASE_NAME);

        assertThat(caughtException())
                .isNotNull()
                .isInstanceOf(CantCreateDatabaseException.class);
    }

    @Test
    public void CreateDatabase_CantCreateTables_ThrowsCantCreateDatabaseException() throws Exception {

        when(mockDatabaseFactory.newTableFactory(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.REPOSITORIES_TABLE_NAME)).thenReturn(null);

        databaseFactory = new NetworkserviceswalletresourcesDatabaseFactory(mockPluginDatabaseSystem);
        catchException(databaseFactory).createDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.DATABASE_NAME);

        assertThat(caughtException())
                .isNotNull()
                .isInstanceOf(CantCreateDatabaseException.class);
    }


    @Test
    public void CreateDatabase_ConflictedIdWhenCreatingTables_ThrowsCantCreateDatabaseException() throws Exception {
        when(mockDatabaseFactory.newTableFactory(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.REPOSITORIES_TABLE_NAME)).thenReturn(null);

        databaseFactory = new NetworkserviceswalletresourcesDatabaseFactory(mockPluginDatabaseSystem);
        catchException(databaseFactory).createDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.DATABASE_NAME);

        assertThat(caughtException())
                .isNotNull()
                .isInstanceOf(CantCreateDatabaseException.class);
    }


    @Test
    public void CreateDatabase_GeneralExceptionThrown_ThrowsCantCreateDatabaseException() throws Exception {
        when(mockDatabase.getDatabaseFactory()).thenReturn(null);

        databaseFactory = new NetworkserviceswalletresourcesDatabaseFactory(mockPluginDatabaseSystem);

        catchException(databaseFactory).createDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.DATABASE_NAME);

        assertThat(caughtException())
                .isNotNull()
                .isInstanceOf(CantCreateDatabaseException.class);
    }
}

