package com.bitdubai.sub_app.developer.fragment;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_pip_api.layer.module.developer.interfaces.ToolManager;
import com.bitdubai.sub_app.developer.R;
import com.bitdubai.sub_app.developer.common.Databases;
import com.bitdubai.sub_app.developer.common.DatabasesTable;
import com.bitdubai.sub_app.developer.common.Resource;
import com.bitdubai.sub_app.developer.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments.DatabaseToolsDatabaseListFragment</code>
 * haves all methods for the database tools activity of a developer
 * <p/>
 * <p/>
 * Created by Mati
 *
 * @version 1.0
 */
public class DatabaseToolsDatabaseTableListFragment extends AbstractFermatFragment<ReferenceAppFermatSession<ToolManager>, ResourceProviderManager> {

    View rootView;
    private ErrorManager errorManager;
    private DeveloperDatabase developerDatabase;
    private List<DatabasesTable> lstTables;
    List<DeveloperDatabaseTable> developerDatabaseTableList;

    public void setResource(Resource databases) {
        this.databases = databases;
    }

    private Resource databases;
    private GridView gridView;

    public static DatabaseToolsDatabaseTableListFragment newInstance() {
        return new DatabaseToolsDatabaseTableListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databases = (Resource) appSession.getData("resource");
        developerDatabase = (DeveloperDatabase) appSession.getData("database");

        errorManager = appSession.getErrorManager();
        setRetainInstance(true);
        try {
            ToolManager toolManager = appSession.getModuleManager();
        } catch (Exception ex) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(ex));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_database_tools, container, false);

        lstTables = new ArrayList<>();

        gridView = (GridView) rootView.findViewById(R.id.gridView);

        try {
            if (databases.type == Databases.TYPE_ADDON) {
                AddonVersionReference addon = AddonVersionReference.getByKey(databases.code);
                this.developerDatabaseTableList = appSession.getModuleManager().getAddonTableListFromDatabase(addon, developerDatabase);
            } else if (databases.type == Databases.TYPE_PLUGIN) {
                this.developerDatabaseTableList = appSession.getModuleManager().getPluginTableListFromDatabase(databases.pluginVersionReference, developerDatabase);
            }

            for (int i = 0; i < developerDatabaseTableList.size(); i++) {
                DatabasesTable item = new DatabasesTable();

                item.picture = "databases";
                item.databases = developerDatabaseTableList.get(i).getName();
                item.type = Resource.TYPE_PLUGIN;
                lstTables.add(item);

            }

            Configuration config = getResources().getConfiguration();
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                gridView.setNumColumns(6);
            } else {
                gridView.setNumColumns(3);
            }

            AppListAdapter _adpatrer = new AppListAdapter(getActivity(), R.layout.developer_app_grid_item, lstTables);
            _adpatrer.notifyDataSetChanged();
            gridView.setAdapter(_adpatrer);

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();

        }
        return rootView;
    }

    public class AppListAdapter extends ArrayAdapter<DatabasesTable> {


        public AppListAdapter(Context context, int textViewResourceId, List<DatabasesTable> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final DatabasesTable item = getItem(position);

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.developer_app_grid_item, parent, false);

                holder = new ViewHolder();

                holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        DatabaseToolsDatabaseTableRecordListFragment dabaDatabaseToolsDatabaseTableListFragment = new DatabaseToolsDatabaseTableRecordListFragment();

                        dabaDatabaseToolsDatabaseTableListFragment.setResource(databases);
                        dabaDatabaseToolsDatabaseTableListFragment.setDeveloperDatabaseTable(developerDatabaseTableList.get(position));
                        dabaDatabaseToolsDatabaseTableListFragment.setDeveloperDatabase(developerDatabase);

                        //set the next fragment and params

                        appSession.setData("resource", databases);
                        appSession.setData("developerDataBase", developerDatabase);
                        appSession.setData("databaseTable", developerDatabaseTableList.get(position));

//                        ((FermatScreenSwapper)getActivity()).changeScreen(DeveloperFragmentsEnumType.CWP_WALLET_DEVELOPER_TOOL_DATABASE_TABLE_RECORD_LIST_FRAGMENT.getKey(),R.id.startContainer,null);
                        changeActivity(Activities.CWP_WALLET_DEVELOPER_TOOL_DATABASE_TABLE_RECORD_LIST, appSession.getAppPublicKey());
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView textView = (TextView) convertView.findViewById(R.id.company_text_view);
            String formatedString = StringUtils.replaceStringByUnderScore(item.databases);
            formatedString = StringUtils.splitCamelCase(formatedString);
            textView.setText(formatedString);

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CaviarDreams.ttf");
            textView.setTypeface(tf);
            holder.companyTextView = textView;

            switch (item.picture) {
                case "plugin":
                    holder.imageView.setImageResource(R.drawable.table);
                    holder.imageView.setTag("DeveloperRecordsFragment");
                    break;
                case "addon":
                    holder.imageView.setImageResource(R.drawable.table);
                    holder.imageView.setTag("DeveloperRecordsFragment");
                    break;
                default:
                    holder.imageView.setImageResource(R.drawable.table);
                    holder.imageView.setTag("DeveloperRecordsFragment");
                    break;
            }

            return convertView;
        }

    }

    /**
     * ViewHolder.
     */
    private class ViewHolder {
        public ImageView imageView;
        public TextView companyTextView;
    }
}