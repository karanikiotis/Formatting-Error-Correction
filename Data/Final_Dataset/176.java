package com.lechucksoftware.proxy.proxysettings.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.db.PacEntity;
import com.lechucksoftware.proxy.proxysettings.utils.UIUtils;

import java.util.List;

public class PacListAdapter extends ArrayAdapter<PacEntity>
{
    private static final String TAG = PacListAdapter.class.getSimpleName();
    private final LayoutInflater vi;
    private Context ctx;

    public PacListAdapter(Context context)
    {
        super(context, R.layout.proxy_list_item);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ctx = context;
    }

    static class ApViewHolder
    {
        TextView pacUrlFile;
        TextView used;
        LinearLayout usedLayout;
    }

    public void setData(List<PacEntity> confList)
    {
//        App.getTraceUtils().startTrace(TAG, "setData", Log.INFO);

        Boolean needsListReplace = false;

        if (this.getCount() == confList.size())
        {
            // Check if the order of SSID is changed
            for (int i = 0; i < this.getCount(); i++)
            {
                PacEntity adapterPacItem = this.getItem(i);
                PacEntity newPacItem = confList.get(i);

                if (!adapterPacItem.equals(newPacItem))
                {
                    // Changed the Proxies order
//                    Timber.d("setData order: Expecting %s, Found %s", newPacItem, adapterPacItem);
                    needsListReplace = true;
                    break;
                }
            }
        }
        else
        {
            needsListReplace = true;
        }

        if (needsListReplace)
        {
            setNotifyOnChange(false);
            clear();
            addAll(confList);
//            App.getTraceUtils().partialTrace(TAG,"setData","Replaced adapter list items",Log.DEBUG);

            // note that a call to notifyDataSetChanged() implicitly sets the setNotifyOnChange back to 'true'!
            // That's why the call 'setNotifyOnChange(false) should be called first every time (see call before 'clear()').
            notifyDataSetChanged();
//            App.getTraceUtils().partialTrace(TAG,"setData","notifyDataSetChanged",Log.DEBUG);
        }
        else
        {
            // Just notifyDataSetChanged
            notifyDataSetChanged();
//            App.getTraceUtils().partialTrace(TAG,"setData","notifyDataSetChanged",Log.DEBUG);
        }

//        App.getTraceUtils().stopTrace(TAG, "setData", Log.INFO);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ApViewHolder viewHolder;
        View view = convertView;

        if (view == null)
        {
            view = vi.inflate(R.layout.pac_list_item, parent, false);

            viewHolder = new ApViewHolder();
            viewHolder.pacUrlFile = (TextView) view.findViewById(R.id.list_item_pac_url_file);
            viewHolder.used = (TextView) view.findViewById(R.id.li_pac_used_txt);
            viewHolder.usedLayout = (LinearLayout) view.findViewById(R.id.li_pac_used_layout);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ApViewHolder) view.getTag();
        }

        PacEntity listItem = getItem(position);

        if (listItem != null)
        {
            viewHolder.pacUrlFile.setText(listItem.getPacUriFile().toString());
            viewHolder.used.setText(String.valueOf(listItem.getUsedByCount()));
            viewHolder.usedLayout.setVisibility(UIUtils.booleanToVisibility(listItem.getInUse()));
        }

        return view;
    }
}
