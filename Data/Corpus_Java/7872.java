package unit.com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.structure.database.asset_issuing_transaction_dao;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;

import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.version_1.exceptions.CantCheckAssetIssuingProgressException;
import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.version_1.structure.database.AssetIssuingDAO;
import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.version_1.structure.database.AssetIssuingDatabaseConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by frank on 31/10/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetNumberOfIssuedAssetsTest {
    AssetIssuingDAO assetIssuingDAO;
    UUID pluginId;

    @Mock
    PluginDatabaseSystem pluginDatabaseSystem;

    @Mock
    Database database;

    @Mock
    DatabaseTable databaseTable;

    @Mock
    DatabaseTableRecord databaseTableRecord;

    String transactionId = "transactionId";
    List<DatabaseTableRecord> records;
    List<DatabaseTableRecord> recordsForException;
    String publicKey = "publicKey";
    int issuedAssetsExpected = 1;

    @Before
    public void setUp() throws Exception {
        pluginId = UUID.randomUUID();

        when(pluginDatabaseSystem.openDatabase(pluginId, AssetIssuingDatabaseConstants.ASSET_ISSUING_DATABASE)).thenReturn(database);
        assetIssuingDAO = new AssetIssuingDAO(pluginDatabaseSystem, pluginId);

        records = new LinkedList<>();
        records.add(databaseTableRecord);

        recordsForException = new LinkedList<>();
        recordsForException.add(databaseTableRecord);
        recordsForException.add(databaseTableRecord);

        mockitoRules();
    }

    private void mockitoRules() throws Exception {
        when(database.getTable(AssetIssuingDatabaseConstants.ASSET_ISSUING_TABLE_NAME)).thenReturn(databaseTable);
        when(databaseTable.getRecords()).thenReturn(records);
        when(databaseTableRecord.getIntegerValue(AssetIssuingDatabaseConstants.ASSET_ISSUING_ASSETS_COMPLETED_COLUMN_NAME)).thenReturn(issuedAssetsExpected);
    }

    @Test
    public void test_OK() throws Exception {
        int issuedAssets = assetIssuingDAO.getNumberOfIssuedAssets(publicKey);
        assertThat(issuedAssets).isEqualTo(issuedAssetsExpected);
    }

    @Test
    public void test_Throws_CantCheckAssetIssuingProgressException() throws Exception {
        when(databaseTable.getRecords()).thenReturn(recordsForException);
        catchException(assetIssuingDAO).getNumberOfIssuedAssets(publicKey);
        Exception thrown = caughtException();
        assertThat(thrown)
                .isNotNull()
                .isInstanceOf(CantCheckAssetIssuingProgressException.class);
    }
}
