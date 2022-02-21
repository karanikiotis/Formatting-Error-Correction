package com.bitdubai.sub_app.crypto_broker_identity.fragmentFactory;

import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;

/**
 * Created by Matias Furszyfer on 2015.07.22..
 */

public enum CryptoBrokerIdentityFragmentsEnumType implements FermatFragmentsEnumType<CryptoBrokerIdentityFragmentsEnumType> {

    CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_MAIN_FRAGMENT("CBPSACBIMF"),
    CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_IMAGE_CROPPER_FRAGMENT("CBPSACBIICF"),
    CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_CREATE_IDENTITY_FRAGMENT("CBPSACBICIF"),
    CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_EDIT_IDENTITY_FRAGMENT("CBPSACBIEIF"),
    CBP_SUB_APP_CRYPTO_BROKER_IDENTITY_GEOLOCATION_IDENTITY_FRAGMENT("CBPSACBIGIF");

    private String key;

    CryptoBrokerIdentityFragmentsEnumType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }


    @Override
    public String toString() {
        return key;
    }

    public static CryptoBrokerIdentityFragmentsEnumType getValue(String name) {
        for (CryptoBrokerIdentityFragmentsEnumType fragments : CryptoBrokerIdentityFragmentsEnumType.values()) {
            if (fragments.key.equals(name)) {
                return fragments;
            }
        }
        return null;
    }
}
