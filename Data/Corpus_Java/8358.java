package   com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_broker.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.UUID;

/**
 *  The Class  <code>com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_broker.developer.bitdubai.version_1.database.Crypto BrokerActorDatabaseFactory</code>
 * is responsible for creating the tables in the database where it is to keep the information.
 * <p/>
 *
 * Created by Angel Veloz - (vlzangel91@gmail.com) on 16/01/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CryptoBrokerActorDatabaseFactory implements DealsWithPluginDatabaseSystem {

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * Constructor with parameters to instantiate class
     * .
     *
     * @param pluginDatabaseSystem DealsWithPluginDatabaseSystem
     */
    public CryptoBrokerActorDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * Create the database
     *
     * @param ownerId      the owner id
     * @param databaseName the database name
     * @return Database
     * @throws CantCreateDatabaseException
     */
    protected Database createDatabase(UUID ownerId, String databaseName) throws CantCreateDatabaseException {
        Database database;

        /**
         * I will create the database where I am going to store the information of this wallet.
         */
        try {
            database = this.pluginDatabaseSystem.createDatabase(ownerId, databaseName);
        } catch (CantCreateDatabaseException cantCreateDatabaseException) {
            /**
             * I can not handle this situation.
             */
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateDatabaseException, "", "Exception not handled by the plugin, There is a problem and i cannot create the database.");
        }

        /**
         * Next, I will add the needed tables.
         */
        try {
            DatabaseTableFactory table;
            DatabaseFactory databaseFactory = database.getDatabaseFactory();

            /**
             * Create Crypto Broker Actor Relationship table.
             */
            table = databaseFactory.newTableFactory(ownerId, CryptoBrokerActorDatabaseConstants.CRYPTO_BROKER_ACTOR_RELATIONSHIP_TABLE_NAME);

            table.addColumn(CryptoBrokerActorDatabaseConstants.CRYPTO_BROKER_ACTOR_RELATIONSHIP_RELATIONSHIP_ID_COLUMN_NAME, DatabaseDataType.TEXT, 36, Boolean.TRUE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.CRYPTO_BROKER_ACTOR_RELATIONSHIP_BROKER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.TEXT, 100, Boolean.FALSE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.CRYPTO_BROKER_ACTOR_RELATIONSHIP_WALLET_COLUMN_NAME, DatabaseDataType.TEXT, 100, Boolean.FALSE);

            table.addIndex(CryptoBrokerActorDatabaseConstants.CRYPTO_BROKER_ACTOR_RELATIONSHIP_FIRST_KEY_COLUMN);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }           /**
             * Create Actor Extra Data table.
             */
            table = databaseFactory.newTableFactory(ownerId, CryptoBrokerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);

            table.addColumn(CryptoBrokerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.TEXT, 100, Boolean.TRUE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.ACTOR_EXTRA_DATA_ALIAS_COLUMN_NAME, DatabaseDataType.TEXT, 100, Boolean.FALSE);

            table.addIndex(CryptoBrokerActorDatabaseConstants.ACTOR_EXTRA_DATA_FIRST_KEY_COLUMN);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }           /**
             * Create Quote Extra Data table.
             */
            table = databaseFactory.newTableFactory(ownerId, CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_TABLE_NAME);

            table.addColumn(CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_QUOTE_ID_COLUMN_NAME, DatabaseDataType.TEXT, 36, Boolean.TRUE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.TEXT, 100, Boolean.FALSE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_MERCHANDISE_COLUMN_NAME, DatabaseDataType.TEXT, 20, Boolean.FALSE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_PAYMENT_CURRENCY_COLUMN_NAME, DatabaseDataType.TEXT, 20, Boolean.FALSE);
            table.addColumn(CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_PRICE_COLUMN_NAME, DatabaseDataType.REAL, 100, Boolean.FALSE);

            table.addIndex(CryptoBrokerActorDatabaseConstants.QUOTE_EXTRA_DATA_FIRST_KEY_COLUMN);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }

        } catch (InvalidOwnerIdException invalidOwnerId) {
            /**
             * This shouldn't happen here because I was the one who gave the owner id to the database file system,
             * but anyway, if this happens, I can not continue.
             */
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, invalidOwnerId, "", "There is a problem with the ownerId of the database.");
        }
        return database;
    }

    /**
     * DealsWithPluginDatabaseSystem Interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }
}
