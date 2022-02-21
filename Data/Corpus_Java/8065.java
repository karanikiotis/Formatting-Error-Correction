package com.qiwenge.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.entity.MainMenuItem;

import java.util.List;

public class TabAdapter extends MyBaseAdapter<MainMenuItem> {

    private ViewHolder viewHolder;

    public TabAdapter(Context context, List<MainMenuItem> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookcity_tab, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MainMenuItem model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.title);
            if (model.selected) {
                viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.main_dress_color));
            } else {
                viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.tv_desc_color));
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
    }

}
