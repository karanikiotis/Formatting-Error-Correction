/*
package unit.com.bitdubai.fermat_ccp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.WalletContactMiddlewareSearch;


import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_ccp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.database.WalletContactsMiddlewareDao;
import com.bitdubai.fermat_ccp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.WalletContactsMiddlewareSearch;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.search.SearchField;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.search.SearchOrder;
import ErrorManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;


*/
/**
 * Created by natalia on 11/09/15.
 *//*

public class SetterTest {
    private WalletContactsMiddlewareDao walletContactsMiddlewareDao;

    @Mock
    private SearchField mockSearchField;

    @Mock
    private SearchOrder mockSearchOrder;
    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;

    @Mock
    private ErrorManager mockErrorManager;


    private WalletContactsMiddlewareSearch walletContactsMiddlewareSearch;
    @Before
    public void setUp() throws Exception {

        walletContactsMiddlewareDao = new WalletContactsMiddlewareDao(mockPluginDatabaseSystem, UUID.randomUUID());

        walletContactsMiddlewareSearch = new WalletContactsMiddlewareSearch(mockErrorManager,walletContactsMiddlewareDao);
    }

    @Test
    public void setActorAliasTest() throws Exception {

        catchException(walletContactsMiddlewareSearch).setActorAlias("alias", true);
        assertThat(caughtException()).isNull();

    }

    @Test
    public void setActorFirstNameTest() throws Exception {

        catchException(walletContactsMiddlewareSearch).setActorFirstName("name", true);
        assertThat(caughtException()).isNull();

    }

    @Test
    public void setActorLastNameTest() throws Exception {

        catchException(walletContactsMiddlewareSearch).setActorLastName("lastName", true);
        assertThat(caughtException()).isNull();

    }


}

*/
