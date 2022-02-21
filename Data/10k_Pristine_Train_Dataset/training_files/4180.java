package org.fermat.fermat_dap_android_wallet_asset_user.v2.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_user.sessions.AssetUserSessionReferenceApp;
import org.fermat.fermat_dap_android_wallet_asset_user.v2.common.data.DataManager;
import org.fermat.fermat_dap_android_wallet_asset_user.v2.models.Asset;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.AssetUserSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.interfaces.AssetUserWalletSubAppModuleManager;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;

import static android.widget.Toast.makeText;
import static org.fermat.fermat_dap_android_wallet_asset_user.sessions.SessionConstantsAssetUser.IC_ACTION_USER_ASSET_APPROPRIATE;
import static org.fermat.fermat_dap_android_wallet_asset_user.sessions.SessionConstantsAssetUser.IC_ACTION_USER_ASSET_REDEEM;
import static org.fermat.fermat_dap_android_wallet_asset_user.sessions.SessionConstantsAssetUser.IC_ACTION_USER_ASSET_TRANSFER;
import static org.fermat.fermat_dap_android_wallet_asset_user.sessions.SessionConstantsAssetUser.IC_ACTION_USER_HELP_DETAIL;
import static org.fermat.fermat_dap_android_wallet_asset_user.sessions.SessionConstantsAssetUser.IC_ACTION_USER_ITEM_SELL;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 3/1/16.
 */
public class DetailFragment extends AbstractFermatFragment<AssetUserSessionReferenceApp, ResourceProviderManager> {
    //DATA
    private Asset asset;
    private DataManager dataManager;

    //UI
    private View rootView;
    private Toolbar toolbar;
    private Resources res;

    private ImageView detailAssetImage;
    private FermatTextView detailAssetName;
    private FermatTextView detailAction;
    private FermatTextView detailActorName;
    private FermatTextView detailDataAssetMemo;
    private FermatTextView detailDataAssetAmount;
    private FermatTextView detailDataAssetExpDate;

    //FERMAT
    private ErrorManager errorManager;
    private SettingsManager<AssetUserSettings> settingsManager;
    private AssetUserWalletSubAppModuleManager moduleManager;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        moduleManager = appSession.getModuleManager();
        errorManager = appSession.getErrorManager();
        dataManager = new DataManager(moduleManager);

        configureToolbar();
    }

    private void configureToolbar() {
        toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.dap_user_wallet_toolbar));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBottom(Color.WHITE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.dap_user_wallet_toolbar));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dap_v2_wallet_asset_user_detail, container, false);
        res = rootView.getResources();

        setupUI();
        setupUIData();

        return rootView;
    }

    private void setupUI() {
        detailAssetImage = (ImageView) rootView.findViewById(R.id.detailAssetImage);
        detailAssetName = (FermatTextView) rootView.findViewById(R.id.detailAssetName);
        detailAction = (FermatTextView) rootView.findViewById(R.id.detailAction);
        detailActorName = (FermatTextView) rootView.findViewById(R.id.detailActorName);
        detailDataAssetMemo = (FermatTextView) rootView.findViewById(R.id.detailDataAssetMemo);
        detailDataAssetAmount = (FermatTextView) rootView.findViewById(R.id.detailDataAssetAmount);
        detailDataAssetExpDate = (FermatTextView) rootView.findViewById(R.id.detailDataAssetExpDate);
    }

    private void setupUIData() {
//        asset = (Asset) appSession.getData("asset");
        try {
            asset = dataManager.getIssuers().get(0).getAssets().get(0);

            byte[] img = (asset.getImage() == null) ? new byte[0] : asset.getImage();
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(detailAssetImage, res, R.drawable.img_asset_without_image, false);
            bitmapWorkerTask.execute(img);

            detailAssetName.setText(asset.getName());
            detailAction.setText(res.getString(R.string.dap_user_wallet_v2_detail_received));
            detailActorName.setText(asset.getActorName());
            detailDataAssetMemo.setText("Memo Test");
            detailDataAssetAmount.setText(asset.getFormattedAmount());
            detailDataAssetExpDate.setText(asset.getFormattedExpDate());
        } catch (CantLoadWalletException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(0, IC_ACTION_USER_ASSET_REDEEM, 0, res.getString(R.string.dap_user_wallet_action_redeem))
                .setIcon(R.drawable.ic_redeem)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, IC_ACTION_USER_ASSET_TRANSFER, 0, res.getString(R.string.dap_user_wallet_action_transfer))
                .setIcon(R.drawable.ic_transfer)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, IC_ACTION_USER_ASSET_APPROPRIATE, 0, res.getString(R.string.dap_user_wallet_action_appropriate))
                .setIcon(R.drawable.ic_appropriate_user_wallet)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, IC_ACTION_USER_ITEM_SELL, 0, res.getString(R.string.dap_user_wallet_action_sell))
                .setIcon(R.drawable.ic_sell)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, IC_ACTION_USER_HELP_DETAIL, 0, res.getString(R.string.help))
                .setIcon(R.drawable.dap_asset_user_help_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            if (id == IC_ACTION_USER_ASSET_REDEEM) {
                changeActivity(Activities.DAP_WALLET_ASSET_USER_V2_REDEEM_POINTS, appSession.getAppPublicKey());
            } else if (id == IC_ACTION_USER_ASSET_TRANSFER) {

            } else if (id == IC_ACTION_USER_ASSET_APPROPRIATE) {

            } else if (id == IC_ACTION_USER_ITEM_SELL) {

            } else if (id == IC_ACTION_USER_HELP_DETAIL) {

            }
        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), getResources().getString(R.string.dap_user_wallet_system_error),
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
