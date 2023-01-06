package test.com.bitdubai.fermat_tky_plugin.layer.identity.artist_identity.developer.bitdubai.version_1.database.tokenlyArtistIdentityDao;

import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_tky_api.layer.identity.artist.exceptions.CantListArtistIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.artist.interfaces.Artist;
import com.bitdubai.fermat_tky_plugin.layer.identity.artist_identity.developer.bitdubai.version_1.database.TokenlyArtistIdentityDao;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by gianco on 09/05/16.
 */
public class GetIdentityArtistsFromCurrentDeviceUserTest {
    @Mock
    List<Artist> artists;
    @Mock
    DeviceUser deviceUser;
    @Test
    public void getIdentityArtistsFromCurrentDeviceUserTest() throws CantListArtistIdentitiesException {
        TokenlyArtistIdentityDao tokenlyArtistIdentityDao = Mockito.mock(TokenlyArtistIdentityDao.class);
        when(tokenlyArtistIdentityDao.getIdentityArtistsFromCurrentDeviceUser(deviceUser)).thenReturn(artists);
    }
}
