package org.fermat.fermat_dap_android_wallet_asset_issuer.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_issuer.common.adapters.UserDeliveryListAdapter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.Data;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.UserDelivery;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.SessionConstantsAssetIssuer;
import org.fermat.fermat_dap_android_wallet_asset_issuer.util.CommonLogger;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

/**
 * Created by frank on 12/22/15.
 */
public class UserDeliveryListFragment extends FermatWalletListFragment<UserDelivery, ReferenceAppFermatSession<AssetIssuerWalletSupAppModuleManager>, ResourceProviderManager> {

    // Constants
    private static final String TAG = "UserDeliveryListFragment";

    // Fermat Managers
    private AssetIssuerWalletSupAppModuleManager moduleManager;
    private ErrorManager errorManager;
    // Data
    private List<UserDelivery> users;
    private DigitalAsset digitalAsset;

//    SettingsManager<AssetIssuerSettings> settingsManager;

    //UI
    private View noUsersView;
    private View rootView;
    private FermatTextView assetDeliveryListNameText;
    private FermatTextView assetDeliveryListRemainingText;
    private ImageView assetDeliveryListImage;

    public static UserDeliveryListFragment newInstance() {
        return new UserDeliveryListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            errorManager = appSession.getErrorManager();
            moduleManager = appSession.getModuleManager();

            users = getMoreDataAsync(FermatRefreshTypes.NEW, 0);
        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.DAP_ASSET_ISSUER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        setupBackgroundBitmap(layout);
        configureToolbar();

        rootView = layout;
        setupUI();
        setupUIData();

        noUsersView = layout.findViewById(R.id.dap_wallet_asset_issuer_no_users);

        showOrHideNoUsersView(users.isEmpty());
    }

    private void setUpHelpAssetDeliveryList(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_issuer_wallet)
                    .setIconRes(R.drawable.asset_issuer)
                    .setVIewColor(R.color.dap_issuer_view_color)
                    .setSubTitle(R.string.dap_issuer_wallet_delivery_list_subTitle)
                    .setBody(R.string.dap_issuer_wallet_delivery_list_body)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, SessionConstantsAssetIssuer.IC_ACTION_ISSUER_HELP_DELIVER, 0, "Help")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetIssuer.IC_ACTION_ISSUER_HELP_DELIVER) {
                setUpHelpAssetDeliveryList(moduleManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), R.string.dap_issuer_wallet_system_error,
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        assetDeliveryListImage = (ImageView) rootView.findViewById(R.id.assetDeliveryListImage);
        assetDeliveryListNameText = (FermatTextView) rootView.findViewById(R.id.assetDeliveryListNameText);
        assetDeliveryListRemainingText = (FermatTextView) rootView.findViewById(R.id.assetDeliveryListRemainingText);
    }

    private void setupUIData() {
        digitalAsset = (DigitalAsset) appSession.getData("asset_data");

        if (digitalAsset.getImage() != null) {
            assetDeliveryListImage.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(digitalAsset.getImage())));
        } else {
            assetDeliveryListImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.img_asset_without_image));
        }

        assetDeliveryListNameText.setText(digitalAsset.getName());
        assetDeliveryListRemainingText.setText(digitalAsset.getAvailableBalanceQuantity() + " Assets Remaining");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        try {
//            IssuerWalletNavigationViewPainter navigationViewPainter = new IssuerWalletNavigationViewPainter(getActivity(), null);
//            getPaintActivtyFeactures().addNavigationView(navigationViewPainter);
//        } catch (Exception e) {
//            makeText(getActivity(), "Oops! recovering from system error", Toast.LENGTH_SHORT).show();
//            errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
//        }
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            toolbar.setBottom(Color.WHITE);
            Drawable drawable = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                drawable = getResources().getDrawable(R.drawable.dap_wallet_asset_issuer_action_bar_gradient_colors, null);
            else
                drawable = getResources().getDrawable(R.drawable.dap_wallet_asset_issuer_action_bar_gradient_colors);
            toolbar.setBackground(drawable);
        }
    }

    private void setupBackgroundBitmap(final View rootView) {
        AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {

            WeakReference<ViewGroup> view;

            @Override
            protected void onPreExecute() {
                view = new WeakReference(rootView);
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap drawable = null;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = true;
                    options.inSampleSize = 5;
                    drawable = BitmapFactory.decodeResource(
                            getResources(), R.drawable.bg_app_image, options);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
                return drawable;
            }

            @Override
            protected void onPostExecute(Bitmap drawable) {
                if (drawable != null) {
                    view.get().setBackground(new BitmapDrawable(getResources(), drawable));
                }
            }
        };
        asyncTask.execute();
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_wallet_asset_issuer_user_delivery_list;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_wallet_asset_issuer_user_delivery_list_activity_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                users = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(users);

                showOrHideNoUsersView(users.isEmpty());
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new UserDeliveryListAdapter(getActivity(), users, moduleManager);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    private void showOrHideNoUsersView(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            noUsersView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noUsersView.setVisibility(View.GONE);
        }
    }

    @Override
    public List<UserDelivery> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<UserDelivery> users = new ArrayList<>();
        if (moduleManager != null) {
            try {
                if (digitalAsset == null)
                    digitalAsset = (DigitalAsset) appSession.getData("asset_data");
                users = Data.getUserDeliveryList(WalletUtilities.WALLET_PUBLIC_KEY, digitalAsset, moduleManager);

            } catch (Exception ex) {
                CommonLogger.exception(TAG, ex.getMessage(), ex);
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(
                            Wallets.DAP_ASSET_ISSUER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                            ex);
            }
        } else {
            Toast.makeText(getActivity(),
                    R.string.dap_issuer_wallet_system_error,
                    Toast.LENGTH_SHORT).
                    show();
        }
        return users;
    }
}
