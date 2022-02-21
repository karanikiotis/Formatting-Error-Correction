package ChatActorNetworkServiceSearch;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedAddonsExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPlatformExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.DiscoveryQueryParameters;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformComponents;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_cht_plugin.layer.actor_network_service.chat.developer.bitdubai.version_1.structure.ChatActorNetworkServiceSearch;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsClientConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsVPNConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantEstablishConnectionException;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRegisterComponentException;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRequestListException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Miguel Rincon on 4/14/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class ConstructionTest {

    private final CommunicationsClientConnection mCommunicationsClientConnection = new CommunicationsClientConnection() {
        @Override
        public PlatformComponentProfile constructPlatformComponentProfileFactory(String identityPublicKey, String alias, String name, NetworkServiceType networkServiceType, PlatformComponentType platformComponentType, String extraData) {
            return null;
        }

        @Override
        public PlatformComponentProfile constructBasicPlatformComponentProfileFactory(String identityPublicKey, NetworkServiceType networkServiceType, PlatformComponentType platformComponentType) {
            return null;
        }

        @Override
        public DiscoveryQueryParameters constructDiscoveryQueryParamsFactory(PlatformComponentType platformComponentType, NetworkServiceType networkServiceType, String alias, String identityPublicKey, Location location, Double distance, String name, String extraData, Integer offset, Integer max, PlatformComponentType fromOtherPlatformComponentType, NetworkServiceType fromOtherNetworkServiceType) {
            return null;
        }

        @Override
        public void registerComponentForCommunication(NetworkServiceType networkServiceNetworkServiceTypeApplicant, PlatformComponentProfile platformComponentProfile) throws CantRegisterComponentException {

        }

        @Override
        public void updateRegisterActorProfile(NetworkServiceType networkServiceNetworkServiceTypeApplicant, PlatformComponentProfile platformComponentProfile) throws CantRegisterComponentException {

        }

        @Override
        public void requestListComponentRegistered(PlatformComponentProfile networkServiceApplicant, DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestListException {

        }

        @Override
        public List<PlatformComponentProfile> requestListComponentRegistered(DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestListException {
            return null;
        }

        @Override
        public List<PlatformComponentProfile> requestListComponentRegisteredSocket(DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestListException {
            return null;
        }

        @Override
        public void requestVpnConnection(PlatformComponentProfile applicant, PlatformComponentProfile remoteDestination) throws CantEstablishConnectionException {

        }

        @Override
        public void requestDiscoveryVpnConnection(PlatformComponentProfile applicantParticipant, PlatformComponentProfile applicantNetworkService, PlatformComponentProfile remoteParticipant) throws CantEstablishConnectionException {

        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public boolean isRegister() {
            return false;
        }

        @Override
        public CommunicationsVPNConnection getCommunicationsVPNConnectionStablished(NetworkServiceType networkServiceType, PlatformComponentProfile remotePlatformComponentProfile) {
            return null;
        }

        @Override
        public void closeMainConnection() {

        }
    };
    private final ErrorManager mErrorManager = new ErrorManager() {
        @Override
        public void reportUnexpectedPlatformException(PlatformComponents exceptionSource, UnexpectedPlatformExceptionSeverity unexpectedPlatformExceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedPluginException(Plugins exceptionSource, UnexpectedPluginExceptionSeverity unexpectedPluginExceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedWalletException(Wallets exceptionSource, UnexpectedWalletExceptionSeverity unexpectedWalletExceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedAddonsException(Addons exceptionSource, UnexpectedAddonsExceptionSeverity unexpectedAddonsExceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedSubAppException(SubApps exceptionSource, UnexpectedSubAppExceptionSeverity unexpectedAddonsExceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedUIException(UISource exceptionSource, UnexpectedUIExceptionSeverity unexpectedAddonsExceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedPluginException(PluginVersionReference exceptionSource, UnexpectedPluginExceptionSeverity exceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedAddonsException(AddonVersionReference exceptionSource, UnexpectedAddonsExceptionSeverity exceptionSeverity, Exception exception) {

        }

        @Override
        public void reportUnexpectedEventException(FermatEvent exceptionSource, Exception exception) {

        }
    };
    private final PluginVersionReference mPluginVersionReference = new PluginVersionReference(new Version());

    @Test
    public void contructionWithValidParametersNewObjectCreated() {
        ChatActorNetworkServiceSearch chatActorNetworkServiceSearch = new ChatActorNetworkServiceSearch(
                this.mCommunicationsClientConnection,
                this.mErrorManager,
                this.mPluginVersionReference
        );
        assertThat(chatActorNetworkServiceSearch).isNotNull();
    }

}
