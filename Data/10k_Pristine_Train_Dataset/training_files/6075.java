package com.bitdubai.sub_app.crypto_broker_community.common.navigationDrawer;

import android.content.Context;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.MenuItem;
import com.bitdubai.sub_app.crypto_broker_community.R;

/**
 * Created by Nelson Ramirez
 *
 * @since 18/12/2015
 */
@Deprecated
public class BrokerCommunityWalletNavigationViewAdapter extends FermatAdapter<MenuItem, NavigationItemMenuViewHolder> {

    protected BrokerCommunityWalletNavigationViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected NavigationItemMenuViewHolder createHolder(View itemView, int type) {
        return new NavigationItemMenuViewHolder(itemView);
    }

    /**
     * Get custom layout to use it.
     *
     * @return int Layout Resource id: Example: R.layout.row_item
     */
    @Override
    protected int getCardViewResource() {
        return R.layout.cbc_navigation_view_content;
    }

    /**
     * Bind ViewHolder
     *
     * @param holder   ViewHolder object
     * @param data     Object data to render
     * @param position position to render
     */
    @Override
    protected void bindHolder(NavigationItemMenuViewHolder holder, MenuItem data, int position) {
        try {
            holder.getLabel().setText(data.getLabel());

            if (data.isSelected()) {
                bindSelectedMenuItem(holder, position);
            } else {
                bindMenuItem(holder, position);
            }
            data.setSelected(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindMenuItem(NavigationItemMenuViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.getIcon().setImageResource(R.drawable.cbc_ic_nav_friends);
                break;
            case 1:
                holder.getIcon().setImageResource(R.drawable.cbc_ic_nav_connections);
                break;
            case 2:
                holder.getIcon().setImageResource(R.drawable.cbc_ic_nav_notifications);
                break;
        }
    }

    private void bindSelectedMenuItem(NavigationItemMenuViewHolder holder, int position) {
        //holder.getRowContainer().setBackgroundResource(R.color.cbc_selected_menu_item_background);

        switch (position) {
            case 0:
                holder.getIcon().setImageResource(R.drawable.cbc_ic_nav_friends_selected);
                break;
            case 1:
                holder.getIcon().setImageResource(R.drawable.cbc_ic_nav_connections_selected);
                break;
            case 2:
                holder.getIcon().setImageResource(R.drawable.cbc_ic_nav_notifications_selected);
                break;
        }
    }
}
