package com.bitdubai.fermat.dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version1.structure.asset_distribution_transaction_manager;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.version_1.structure.functional.AssetDistributionTransactionManager;
import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.version_1.structure.functional.DigitalAssetDistributor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Created by Luis Campo (campusprize@gmail.com) on 28/10/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetActorAssetIssuerManagerTest {
    private AssetDistributionTransactionManager mockAssetDistributionTransactionManager;
    @Mock
    private ActorAssetIssuerManager actorAssetIssuerManager;
    @Mock
    private AssetVaultManager assetVaultManager;
    @Mock
    private DigitalAssetDistributor digitalAssetDistributor;
    @Mock
    private ErrorManager errorManager;
    @Mock
    private PluginDatabaseSystem pluginDatabaseSystem;
    @Mock
    private PluginFileSystem pluginFileSystem;

    @Before
    public void init() throws Exception {
        mockAssetDistributionTransactionManager = new AssetDistributionTransactionManager(assetVaultManager, errorManager, UUID.randomUUID(), pluginDatabaseSystem,
                pluginFileSystem);
    }

    @Test
    public void setActorAssetIssuerManagerThrowsCantSetObjectExceptionTest() throws CantSetObjectException {
        System.out.println("Probando metodo setActorAssetIssuerManagerThrowsCantSetObjectExceptionTest()");
        try {
            mockAssetDistributionTransactionManager.setActorAssetIssuerManager(null);
            fail("The method didn't throw when I expected it to");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof CantSetObjectException);
        }
    }

    @Test
    public void setActorAssetIssuerManagerTest() throws CantSetObjectException {
        System.out.println("Probando metodo setActorAssetIssuerManagerNoExceptionTest()");
        mockAssetDistributionTransactionManager.setActorAssetIssuerManager(actorAssetIssuerManager);
    }
}
