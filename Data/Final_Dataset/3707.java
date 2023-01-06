package com.lechucksoftware.proxy.proxysettings.ui.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.db.ProxyEntity;
import com.lechucksoftware.proxy.proxysettings.utils.UIUtils;

import java.util.List;

public class ProxiesListAdapter extends ArrayAdapter<ProxyEntity>
{
    private static final String TAG = ProxiesListAdapter.class.getSimpleName();
    private final LayoutInflater vi;
    private final StyleSpan bss;
    private Context ctx;

    public ProxiesListAdapter(Context context)
    {
        super(context, R.layout.proxy_list_item);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ctx = context;

        // Span to set text color to some RGB value
        bss = new StyleSpan(android.graphics.Typeface.BOLD);
    }

    static class ApViewHolder
    {
        TextView host;
        TextView port;
        TextView bypass;
//        TagsView tags;
        TextView used;
        LinearLayout usedLayout;
    }

    public void setData(List<ProxyEntity> confList)
    {
//        App.getTraceUtils().startTrace(TAG, "setData", Log.DEBUG);

        Boolean needsListReplace = false;

        if (this.getCount() == confList.size())
        {
            // Check if the order of SSID is changed
            for (int i = 0; i < this.getCount(); i++)
            {
                ProxyEntity adapterProxyItem = this.getItem(i);
                ProxyEntity newProxyItem = confList.get(i);

                if (!adapterProxyItem.equals(newProxyItem))
                {
                    // Changed the Proxies order
//                    Timber.d("setData order: Expecting %s, Found %s", newProxyItem, adapterProxyItem);
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

//        App.getTraceUtils().stopTrace(TAG, "setData", Log.DEBUG);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ApViewHolder viewHolder;
        View view = convertView;

        if (view == null)
        {
            view = vi.inflate(R.layout.proxy_list_item, parent, false);

            viewHolder = new ApViewHolder();
            viewHolder.host = (TextView) view.findViewById(R.id.list_item_proxy_host);
            viewHolder.port = (TextView) view.findViewById(R.id.list_item_proxy_port);
            viewHolder.bypass = (TextView) view.findViewById(R.id.list_item_proxy_bypass);
//            viewHolder.tags = (TagsView) view.findViewById(R.id.list_item_proxy_tags);
            viewHolder.used = (TextView) view.findViewById(R.id.li_proxy_used_txt);
            viewHolder.usedLayout = (LinearLayout) view.findViewById(R.id.proxy_used_layout);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ApViewHolder) view.getTag();
        }

        ProxyEntity listItem = getItem(position);

        if (listItem != null)
        {
            viewHolder.host.setText(listItem.getHost());
            viewHolder.port.setText(listItem.getPort().toString());

            SpannableStringBuilder ssb = new SpannableStringBuilder();

            String bypassTitle = getContext().getString(R.string.bypass_for);
            ssb.append(bypassTitle);
            ssb.append(" " + UIUtils.CleanExclusion(listItem.getExclusion()));
            ssb.setSpan(bss, 0, bypassTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            viewHolder.bypass.setText(ssb);
            viewHolder.bypass.setVisibility(UIUtils.booleanToVisibility(!TextUtils.isEmpty(listItem.getExclusion())));
//            viewHolder.tags.setTags(listItem.getTags());
            viewHolder.used.setText(String.valueOf(listItem.getUsedByCount()));
            viewHolder.used.setVisibility(UIUtils.booleanToVisibility(listItem.getInUse()));
            viewHolder.usedLayout.setVisibility(UIUtils.booleanToVisibility(listItem.getInUse()));
        }

        return view;
    }
}
