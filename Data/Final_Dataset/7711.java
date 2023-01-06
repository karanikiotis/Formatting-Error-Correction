//package unit.com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.WalletResourcesNetworkServicePluginRoot;
//
//import com.bitdubai.fermat_api.layer.all_definition.github.GitHubConnection;
//import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
//import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
//import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
//import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
//import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
//import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
//import com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.WalletResourcesNetworkServicePluginRoot;
//import com.bitdubai.fermat_wpd_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.structure.Repository;
//import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.ErrorManager;
//import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.enums.EventType;
//import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
//import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;
//import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
//
//import junit.framework.TestCase;
//
//import org.junit.Before;
//import org.junit.Ignore;
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
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.when;
//
///**
// * Created by natalia on 10/09/15.
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class UninstallCompleteWalletTest extends TestCase {
//
//    /**
//     * DealsWithErrors interface Mocked
//     */
//    @Mock
//    ErrorManager errorManager;
//
//    /**
//     * UsesFileSystem Interface member variables.
//     */
//    @Mock
//    PluginFileSystem pluginFileSystem;
//
//
//    /**
//     * DealWithEvents Iianterface member variables.
//     */
//    @Mock
//    private FermatEventListener mockFermatEventListener;
//
//    @Mock
//    private EventManager mockEventManager;
//
//    @Mock
//    private Database mockDatabase;
//
//    @Mock
//    private PluginDatabaseSystem mockPluginDatabaseSystem;
//
//    @Mock
//    private PluginTextFile mockPluginTextFile;
//
//    @Mock
//    private Repository repository;
//
//    @Mock
//    private GitHubConnection githubConnection;
//
//    @Mock
//    private XMLParser mockXMLParser;
//
//    String repoManifest = "<skin ></skin >";
//
//    @Mock
//    FermatEvent mockFermatEvent;
//
//
//    WalletResourcesNetworkServicePluginRoot walletResourcePluginRoot;
//
//    @Before
//    public void setUp() throws Exception {
//
//
//        walletResourcePluginRoot = new WalletResourcesNetworkServicePluginRoot();
//        walletResourcePluginRoot.setPluginFileSystem(pluginFileSystem);
//        walletResourcePluginRoot.setEventManager(mockEventManager);
//        walletResourcePluginRoot.setErrorManager(errorManager);
//
//        walletResourcePluginRoot.setPluginDatabaseSystem(mockPluginDatabaseSystem);
//
//        when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);
//        when(githubConnection.getFile(anyString())).thenReturn(repoManifest);
//
//        when(mockEventManager.getNewListener(EventType.BEGUN_WALLET_INSTALLATION)).thenReturn(mockFermatEventListener);
//        when(pluginFileSystem.getTextFile(any(UUID.class), anyString(), anyString(), any(FilePrivacy.class), any(FileLifeSpan.class))).thenReturn(mockPluginTextFile);
//
//        when(mockEventManager.getNewEvent(EventType.WALLET_UNINSTALLED)).thenReturn(mockFermatEvent);
//
//    }
//
//@Ignore
//    @Test
//    public void testUninstallCompleteWallet() throws Exception {
//        walletResourcePluginRoot.start();
//        //TODO este metodo no retorna exceptions y deberia
//        //TODO error parseando el skin, al parecer la estructura subida al repo no es correcta - se debe actualizar
//        catchException(walletResourcePluginRoot).unninstallCompleteWallet("reference_wallet", "bitcoin_wallet", "bitDubai", "mati_wallet_verde", UUID.randomUUID(), "medium", "navigationStructureVersion", true);
//        assertThat(caughtException()).isNotNull();
//
//    }
//
//
//
//}
