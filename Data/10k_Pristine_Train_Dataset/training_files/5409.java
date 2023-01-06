package com.bitdubai.reference_wallet.crypto_broker_wallet.common.adapters;

import android.content.Context;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.EarningsPair;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.holders.EarningsCurrencyPairsViewHolder;

import java.util.List;

/**
 * Created by nelson on 29/01/16.
 */
public class EarningsCurrencyPairsAdapter extends FermatAdapter<EarningsPair, EarningsCurrencyPairsViewHolder> {
    private int selectedItem;

    public EarningsCurrencyPairsAdapter(Context context, List<EarningsPair> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected EarningsCurrencyPairsViewHolder createHolder(View itemView, int type) {
        return new EarningsCurrencyPairsViewHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.cbw_earnings_currency_pair_item;
    }

    @Override
    protected void bindHolder(EarningsCurrencyPairsViewHolder holder, EarningsPair data, int position) {
        holder.bind(data, selectedItem == position);
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }
}
