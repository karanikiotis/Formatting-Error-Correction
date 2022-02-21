package com.bitdubai.fermat_tky_api.layer.sub_app_module.fan.interfaces;

import com.bitdubai.fermat_api.layer.modules.ModuleSettingsImpl;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_tky_api.all_definitions.enums.ExternalPlatform;
import com.bitdubai.fermat_tky_api.all_definitions.enums.TokenlyAPIStatus;
import com.bitdubai.fermat_tky_api.all_definitions.exceptions.IdentityNotFoundException;
import com.bitdubai.fermat_tky_api.all_definitions.exceptions.TokenlyAPINotAvailableException;
import com.bitdubai.fermat_tky_api.all_definitions.exceptions.WrongTokenlyUserCredentialsException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantCreateFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantGetFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantUpdateFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.FanIdentityAlreadyExistsException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_api.layer.sub_app_module.artist.interfaces.TokenlyArtistPreferenceSettings;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 3/17/16.
 */
public interface TokenlyFanIdentityManagerModule extends
        ModuleManager<
                TokenlyFanPreferenceSettings,
                ActiveActorIdentityInformation>,
        ModuleSettingsImpl<TokenlyFanPreferenceSettings>, Serializable {
    /**
     * Through the method <code>listIdentitiesFromCurrentDeviceUser</code> we can get all the fan
     * identities linked to the current logged device user.
     * @return
     * @throws CantListFanIdentitiesException
     */
    FanIdentitiesList listIdentitiesFromCurrentDeviceUser() throws CantListFanIdentitiesException;

    /**
     *
     * @param userName
     * @param profileImage
     * @param externalPassword
     * @param externalPlatform
     * @return
     * @throws CantCreateFanIdentityException
     * @throws FanIdentityAlreadyExistsException
     */
    Fan createFanIdentity(
            String userName,
            byte[] profileImage,
            String externalPassword,
            ExternalPlatform externalPlatform) throws
            CantCreateFanIdentityException,
            FanIdentityAlreadyExistsException, WrongTokenlyUserCredentialsException;

    /**
     *
     * @param userName
     * @param password
     * @param id
     * @param publicKey
     * @param profileImage
     * @param externalPlatform
     * @throws CantUpdateFanIdentityException
     */
    Fan updateFanIdentity(
            String userName,
            String password,
            UUID id,
            String publicKey,
            byte[] profileImage,
            ExternalPlatform externalPlatform) throws
            CantUpdateFanIdentityException, WrongTokenlyUserCredentialsException;

    /**
     * This method returns a Fan identity
     * @param publicKey
     * @return
     * @throws CantGetFanIdentityException
     * @throws IdentityNotFoundException
     */
    Fan getFanIdentity(UUID publicKey) throws
            CantGetFanIdentityException,
            IdentityNotFoundException;

    /**
     * This method checks if the Tokenly Music API is available.
     * @return
     * @throws TokenlyAPINotAvailableException
     */
    TokenlyAPIStatus getMusicAPIStatus() throws TokenlyAPINotAvailableException;


}
