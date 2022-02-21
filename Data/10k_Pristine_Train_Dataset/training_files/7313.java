package com.bitdubai.fermat.dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version1.structure.asset_distribution_recorder_service;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;

import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.version_1.structure.database.AssetDistributionDao;
import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.version_1.structure.events.AssetDistributionRecorderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Luis Campo (campusprize@gmail.com) on 06/10/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetEventManagerTest {
    @Mock
    private EventManager eventManager;
    private AssetDistributionRecorderService assetDistributionRecorderService;

    private AssetDistributionDao assetDistributionDao = Mockito.mock(AssetDistributionDao.class);

    @Before
    public void init() throws Exception {
        assetDistributionRecorderService = new AssetDistributionRecorderService(assetDistributionDao, eventManager);
    }

    @Test
    public void setEventManagerTest() {
        assetDistributionRecorderService.setEventManager(eventManager);
    }
}
