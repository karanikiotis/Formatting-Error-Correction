package com.qiwenge.android.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ShareActionProvider;

import com.qiwenge.android.R;

public class HomeAwayShareProvider extends ShareActionProvider {

    private final Context mContext;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public HomeAwayShareProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        View chooserView = super.onCreateActionView();
        // Set your drawable here
        Drawable icon = mContext.getResources().getDrawable(R.drawable.icon_menu_share);
        Class<? extends View> clazz = chooserView.getClass();
        // reflect all of this shit so that I can change the icon
        try {
            Method method =
                    clazz.getMethod("setExpandActivityOverflowButtonDrawable", Drawable.class);
            method.invoke(chooserView, icon);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return chooserView;
    }
}
