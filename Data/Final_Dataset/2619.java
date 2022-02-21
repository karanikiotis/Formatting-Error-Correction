/*
package unit.com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers.IncomingCryptoOnBlockchainNetworkWaitingTransferenceExtraUserEventHandler;

import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;
import EventManager;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers.IncomingCryptoOnBlockchainNetworkWaitingTransferenceExtraUserEventHandler;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserEventRecorderService;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.IncomingExtraUserRegistry;

import static org.fest.assertions.api.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

*/
/**
 * Created by jorgegonzalez on 2015.07.03..
 *//*

public class HandleEventTest {

    @Mock
    private EventManager mockEventManager;
    @Mock
    private IncomingExtraUserRegistry mockRegistry;

    private IncomingExtraUserEventRecorderService testRecorderService;
    private IncomingCryptoOnBlockchainNetworkWaitingTransferenceExtraUserEventHandler testEventHandler;

    @Before
    public void setUpRecorderService(){
        testRecorderService = new IncomingExtraUserEventRecorderService(mockEventManager, mockRegistry);
    }

    @Test
    public void HandleEvent_TestRecorderNotStarted_ThrowsException() throws Exception{
        testEventHandler = new IncomingCryptoOnBlockchainNetworkWaitingTransferenceExtraUserEventHandler(testRecorderService);
        catchException(testEventHandler).handleEvent(null);
        assertThat(caughtException()).isInstanceOf(TransactionServiceNotStartedException.class);
    }
}
*/
