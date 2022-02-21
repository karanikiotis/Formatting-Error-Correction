package com.bitdubai.fermat_android_api.engine;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;

/**
 * Created by mati on 02/08/16.
 */
public interface NavigationViewPainterInterface {

    View addNavigationViewHeader();

    FermatAdapter addNavigationViewAdapter();

    ViewGroup addNavigationViewBodyContainer(LayoutInflater layoutInflater, ViewGroup base);

    Bitmap addBodyBackground();

    int addBodyBackgroundColor();

    RecyclerView.ItemDecoration addItemDecoration();

    boolean hasBodyBackground();

    boolean hasClickListener();

}
