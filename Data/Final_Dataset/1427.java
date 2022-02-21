/*
package unit.com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.executors.BitcoinBasicWalletTransactionExecutor;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.util.CryptoHasher;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Action;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import BitcoinWalletBalance;
import BitcoinWalletWallet;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookRegistry;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.exceptions.UnexpectedTransactionException;
import com.bitdubai.fermat_ccp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.executors.BitcoinBasicWalletTransactionExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static com.googlecode.catchexception.CatchException.*;
*/
/**
 * Created by jorgegonzalez on 2015.07.08..
 *//*

@RunWith(MockitoJUnitRunner.class)
public class ExecuteTransactionTest {

    @Mock
    private ActorAddressBookManager mockActorAddressBookManager;
    @Mock
    private ActorAddressBookRegistry mockActorAddressBookRegistry;
    @Mock
    private CryptoWalletWallet mockBitcoinWallet;
    @Mock
    private CryptoWalletBalance mockBalance;

    private Transaction<CryptoTransaction> testTransaction;

    private CryptoBasicWalletTransactionExecutor testExecutor;

    @Before
    public void setUpBalanceMockitoRules() throws Exception{
        when(mockBitcoinWallet.getAvailableBalance()).thenReturn(mockBalance);
        when(mockBitcoinWallet.getBookBalance()).thenReturn(mockBalance);
        when(mockActorAddressBookManager.getActorAddressBookRegistry()).thenReturn(mockActorAddressBookRegistry);
        when(mockActorAddressBookRegistry.getActorAddressBookByCryptoAddress(any(CryptoAddress.class))).thenReturn(new MockActorAddressBookRecord());
    }

    @Test
    public void ExecuteTransaction_OnCryptoNetwork_InvocationSuccessfull() throws Exception {
        testExecutor = new CryptoBasicWalletTransactionExecutor(mockBitcoinWallet, mockActorAddressBookManager);
        testTransaction = setUpTransaction(CryptoStatus.ON_CRYPTO_NETWORK);
        catchException(testExecutor).executeTransaction(testTransaction);
        assertThat(caughtException()).isNull();
    }

    @Test
    public void ExecuteTransaction_OnBlockChain_InvocationSuccessfull() throws Exception {
        testExecutor = new CryptoBasicWalletTransactionExecutor(mockBitcoinWallet, mockActorAddressBookManager);
        testTransaction = setUpTransaction(CryptoStatus.ON_BLOCKCHAIN);
        catchException(testExecutor).executeTransaction(testTransaction);
        assertThat(caughtException()).isNull();
    }

    @Test
    public void ExecuteTransaction_ReverseOnCryptoNetwork_InvocationSuccessfull() throws Exception {
        testExecutor = new CryptoBasicWalletTransactionExecutor(mockBitcoinWallet, mockActorAddressBookManager);
        testTransaction = setUpTransaction(CryptoStatus.REVERSED_ON_CRYPTO_NETWORK);
        catchException(testExecutor).executeTransaction(testTransaction);
        assertThat(caughtException()).isNull();
    }

    @Test
    public void ExecuteTransaction_ReverseOnBlockChain_InvocationSuccessfull() throws Exception {
        testExecutor = new CryptoBasicWalletTransactionExecutor(mockBitcoinWallet, mockActorAddressBookManager);
        testTransaction = setUpTransaction(CryptoStatus.REVERSED_ON_BLOCKCHAIN);
        catchException(testExecutor).executeTransaction(testTransaction);
        assertThat(caughtException()).isNull();
    }

    @Test
    public void ExecuteTransaction_UnsupportedCryptoStatus_InvocationSuccessfull() throws Exception {
        testExecutor = new CryptoBasicWalletTransactionExecutor(mockBitcoinWallet, mockActorAddressBookManager);
        testTransaction = setUpTransaction(CryptoStatus.PENDING_SUBMIT);
        catchException(testExecutor).executeTransaction(testTransaction);
        assertThat(caughtException())
                .isNotNull()
                .isInstanceOf(UnexpectedTransactionException.class);
    }

    private Transaction<CryptoTransaction> setUpTransaction(final CryptoStatus testStatus){
        return new Transaction<>(UUID.randomUUID(), setUpCryptoTransaction(testStatus), Action.APPLY, System.currentTimeMillis());
    }

    private CryptoTransaction setUpCryptoTransaction(final CryptoStatus testStatus){
        String addressFromString = AsymmetricCryptography.generateTestAddress(AsymmetricCryptography.derivePublicKey(AsymmetricCryptography.createPrivateKey()));
        String addressToString = AsymmetricCryptography.generateTestAddress(AsymmetricCryptography.derivePublicKey(AsymmetricCryptography.createPrivateKey()));
        CryptoAddress addressFrom = new CryptoAddress(addressFromString, CryptoCurrency.BITCOIN);
        CryptoAddress addressTo = new CryptoAddress(addressToString, CryptoCurrency.BITCOIN);
        String transactionHash = CryptoHasher.performSha256(AsymmetricCryptography.createPrivateKey());
        return new CryptoTransaction(transactionHash, addressFrom, addressTo, CryptoCurrency.BITCOIN, 1L, testStatus);
    }
}
*/
