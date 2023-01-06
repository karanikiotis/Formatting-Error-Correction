//package com.bitdubai.fermat_android_api.ui.adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.bitdubai.fermat_android_api.ui.expandableRecicler.ChildViewHolder;
//import com.bitdubai.fermat_android_api.ui.expandableRecicler.ParentListItem;
//import com.bitdubai.fermat_android_api.ui.expandableRecicler.ParentViewHolder;
//import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
//import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
//import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.MenuItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Matias Furszyfer on 2015.11.12..
// */
//public abstract class NavigationAdapter< I ,MIH extends FermatViewHolder, HH extends FermatViewHolder,CI>
//        extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//
//    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
//    // IF the view under inflation and population is header or Item
//    private static final int TYPE_ITEM = 1;
//
//
//    // list of item data
//    protected List<I> lstData;
//    protected FermatListItemListeners<CI> itemEventListeners;
//
//    private Context context;
//
//
//    public NavigationAdapter(Context context) {
//        this.context = context;
//        this.lstData = new ArrayList<>();
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        if (viewType == TYPE_HEADER) {
//            HH header = onCreateParentViewHolder(viewGroup);
//            return header;
//        } else if (viewType == TYPE_ITEM) {
//            return onCreateChildViewHolder(viewGroup);
//        } else {
//            throw new IllegalStateException("Incorrect ViewType found");
//        }
//    }
//
//
//
//
//    public void setItemEventListeners(FermatListItemListeners<CI> fermatListItemListeners){
//        itemEventListeners = fermatListItemListeners;
//    }
//
//    public void setLstData(List<I> lstData) {
//        this.lstData = lstData;
//        notifyDataSetChanged();
//    }
//
//
//
//    @Override
//    public H onCreateViewHolder(ViewGroup viewGroup, int type) {
//        return createHolder(LayoutInflater.from(context).inflate(getCardViewResource(), viewGroup, false), type);
//    }
//
//    @Override
//    public void onBindViewHolder(H holder, final int position) {
//        try
//        {
//            // setting up custom listeners
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (eventListeners != null) {
//                        eventListeners.onItemClickListener(getItem(position), position);
//                    }
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (eventListeners != null) {
//                        eventListeners.onLongItemClickListener(getItem(position), position);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            bindHolder(holder, getItem(position), position);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataSet == null ? 0 : dataSet.size();
//    }
//
//    /**
//     * Get item
//     *
//     * @param position int position to get
//     * @return Model object
//     */
//    public CI getItem(final int position) {
//        return lstData != null ? (!lstData.isEmpty() && position < dataSet.size()) ? dataSet.get(position) : null : null;
//    }
//
//    /**
//     * Change whole dataSet and notify the adapter
//     *
//     * @param dataSet new ArrayList of model to change
//     */
//    public void changeDataSet(List<M> dataSet) {
//        this.dataSet = dataSet;
//        notifyDataSetChanged();
//    }
//
//    /**
//     * Add an item to current dataSet
//     *
//     * @param item Item to insert into the dataSet
//     */
//    public void addItem(M item) {
//        if (dataSet == null)
//            return;
//        int position = dataSet.size();
//        dataSet.add(item);
//        notifyItemInserted(position);
//    }
//
//    public void setFermatListEventListener(FermatListItemListeners<M> onEventListeners) {
//        this.eventListeners = onEventListeners;
//    }
//
//    /**
//     * Create a new holder instance
//     *
//     * @param itemView View object
//     * @param type     int type
//     * @return ViewHolder
//     */
//    protected abstract H createHolder(View itemView, int type);
//
//    /**
//     * Get custom layout to use it.
//     *
//     * @return int Layout Resource id: Example: R.layout.row_item
//     */
//    protected abstract int getCardViewResource();
//
//    /**
//     * Bind ViewHolder
//     *
//     * @param holder   ViewHolder object
//     * @param data     Object data to render
//     * @param position position to render
//     */
//    protected abstract void bindHolder(H holder, M data, int position);
//
//
//}
