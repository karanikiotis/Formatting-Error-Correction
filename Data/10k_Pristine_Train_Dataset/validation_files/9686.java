package org.fermat.fermat_dap_plugin.layer.actor.asset.user.developer.version_1.event_handlers;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;

import org.fermat.fermat_dap_api.layer.all_definition.events.NewRequestActorNotificationEvent;
import org.fermat.fermat_dap_plugin.layer.actor.asset.user.developer.version_1.AssetUserActorPluginRoot;

/**
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/09/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NewRequestActorNotificationUserEventHandler implements FermatEventHandler {

    private AssetUserActorPluginRoot assetUserActorPluginRoot;

    public NewRequestActorNotificationUserEventHandler(AssetUserActorPluginRoot assetUserActorPluginRoot) {
        this.assetUserActorPluginRoot = assetUserActorPluginRoot;
    }

    @Override
    public void handleEvent(FermatEvent fermatEvent) throws FermatException {

        if (this.assetUserActorPluginRoot.getStatus() == ServiceStatus.STARTED) {

            if (fermatEvent.getSource().equals(EventSource.ACTOR_ASSET_USER)) {
                System.out.println("ACTOR ASSET USER RECEIVE CONNECTION - handleEvent = " + fermatEvent);
                NewRequestActorNotificationEvent newRequestActorNotificationEvent = (NewRequestActorNotificationEvent) fermatEvent;
             /*
              *  Actor Asset Issuer make the job
              */
                this.assetUserActorPluginRoot.handleNewReceiveRequestActorUserNotificationEvent(
                        newRequestActorNotificationEvent.getActorNotification());
            }
        }
    }
}