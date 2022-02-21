package com.qiwenge.android.adapters.base;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * MyBaseAdapter
 * <p/>
 * Created by John
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public Context context;

    public List<T> data;

    @Override
    public int getCount() {
        return (data == null || data.isEmpty()) ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return (data == null || data.isEmpty()) ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public T get(int position) {
        return (data == null || data.isEmpty()) ? null : data.get(position);
    }

    public List<T> get() {
        return data;
    }

    public void add(T t) {
        if (data != null) {
            data.add(t);
            notifyDataSetChanged();
        }
    }

    public void add(List<T> list) {
        if (data != null) {
            data.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addToTop(T t) {
        if (data != null) {
            data.add(0, t);
            notifyDataSetChanged();
        }
    }

    public void addToTop(List<T> list) {
        if (data != null) {
            data.addAll(0, list);
        }
    }

    public void remove(int position) {
        if (data != null && !data.isEmpty()) {
            data.remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(List<T> list) {
        if (data != null && !data.isEmpty()) {
            data.removeAll(list);
            notifyDataSetChanged();
        }
    }

}
