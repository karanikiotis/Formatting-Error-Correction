package com.lechucksoftware.proxy.proxysettings.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lechucksoftware.proxy.proxysettings.App;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.constants.Measures;
import com.lechucksoftware.proxy.proxysettings.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import be.shouldit.proxy.lib.ProxyStatusItem;
import be.shouldit.proxy.lib.utils.ProxyUtils;

public class InputExclusionList extends LinearLayout
{
    private static final String TAG = InputExclusionList.class.getSimpleName();
    private LinearLayout fieldMainLayout;
    private TextView readonlyValueTextView;
    private LinearLayout bypassContainer;
    private TextView titleTextView;
    private String title;
    //    private boolean fullsize;
    private boolean readonly;
    private String exclusionString = "";
    private Map<UUID, InputField> exclusionInputFieldsMap;
    private UIHandler uiHandler;
    private boolean singleLine;
    private float textSize;
    private float titleSize;
    private ArrayList<ValueChangedListener> mListeners;

    public InputExclusionList(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        exclusionInputFieldsMap = new LinkedHashMap<UUID, InputField>();

        uiHandler = new UIHandler();

        readStyleParameters(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.input_exclusion, this);

        if (v != null)
        {
            fieldMainLayout = (LinearLayout) v.findViewById(R.id.field_main_layout);
            titleTextView = (TextView) v.findViewById(R.id.field_title);
            bypassContainer = (LinearLayout) v.findViewById(R.id.bypass_container);
            bypassContainer.removeAllViews();
            readonlyValueTextView = (TextView) v.findViewById(R.id.field_value_readonly);

            refreshUI();
        }
    }

    private InputField createExclusionInputField()
    {
        InputField inputField;
        inputField = new InputField(getContext());

        // TODO: Show inputfield readonly and enable the edit only on click

        //                    i.setOnClickListener(new OnClickListener()
        //                    {
        //                        @Override
        //                        public void onClick(View view)
        //                        {
        //
        //                        }
        //                    });

        inputField.setPadding(0, 0, 0, 0);
        inputField.setTag(inputField.getUUID());
        inputField.setFullsize(false);
        inputField.setReadonly(readonly);
        inputField.setVisibility(VISIBLE);
        inputField.setHint("Add bypass address");

        inputField.setFieldAction(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UUID idToRemove = (UUID) view.getTag();
                InputField i = exclusionInputFieldsMap.remove(idToRemove);
                bypassContainer.removeView(i);
                uiHandler.callRefreshExclusionList();
            }
        });

        inputField.addTextChangedListener(new BypassTextWatcher(inputField));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);

        bypassContainer.addView(inputField, layoutParams);

        return inputField;
    }

    protected void readStyleParameters(Context context, AttributeSet attributeSet)
    {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.InputFieldExclusion);

        try
        {
            title = a.getString(R.styleable.InputFieldExclusion_title);
            singleLine = a.getBoolean(R.styleable.InputFieldExclusion_singleLine, false);
//            fullsize = a.getBoolean(R.styleable.InputField_fullsize, false);
            readonly = a.getBoolean(R.styleable.InputFieldExclusion_readonly, false);
            titleSize = a.getDimension(R.styleable.InputFieldExclusion_titleSize, Measures.DefaultTitleSize);
            textSize = a.getDimension(R.styleable.InputFieldExclusion_textSize, Measures.DefaultTextFontSize);
        }
        finally
        {
            a.recycle();
        }
    }

    public void setExclusionString(String value)
    {
        App.getTraceUtils().startTrace(TAG, "setExclusionString", Log.DEBUG);

        if (exclusionString == null || !exclusionString.equals(value))
        {
            exclusionString = value;

            String[] exclusionList = null;
            exclusionList = ProxyUtils.parseExclusionList(exclusionString);

            bypassContainer.removeAllViews();
            exclusionInputFieldsMap.clear();

            for (String bypass : exclusionList)
            {
                InputField inputField = createExclusionInputField();
                inputField.setValue(bypass);
                exclusionInputFieldsMap.put(inputField.getUUID(), inputField);
            }
        }

        refreshExclusionList();

        App.getTraceUtils().stopTrace(TAG, "setExclusionString", Log.DEBUG);
    }

    public String getExclusionString()
    {
        List<String> values = new ArrayList<String>();
        for (InputField i : exclusionInputFieldsMap.values())
        {
            String value = i.getValue();
            if (!TextUtils.isEmpty(value))
                values.add(i.getValue());
        }

        String result = TextUtils.join(",", values); // Android ProxyProperties class requires that there are no space between exclusion items
        return result;
    }

    private void refreshUI()
    {
        App.getTraceUtils().startTrace(TAG, "refreshUI", Log.DEBUG, true);
        // Layout
        if (singleLine)
        {
            fieldMainLayout.setOrientation(HORIZONTAL);
            titleTextView.setWidth((int) UIUtils.convertDpToPixel(80, getContext()));
        }
        else
        {
            fieldMainLayout.setOrientation(VERTICAL);
            titleTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        // Title
        if (!TextUtils.isEmpty(title))
        {
            titleTextView.setText(title.toUpperCase());
        }

//        refreshExclusionList();

        titleTextView.setTextSize(titleSize);
        readonlyValueTextView.setTextSize(textSize);

        App.getTraceUtils().stopTrace(TAG, "refreshUI", Log.DEBUG);
    }

    private void refreshExclusionList()
    {
        App.getTraceUtils().startTrace(TAG, "refreshExclusionList", Log.DEBUG);

        if (readonly)
        {
            if (exclusionInputFieldsMap != null && exclusionInputFieldsMap.size() > 0)
            {
                readonlyValueTextView.setVisibility(GONE);

                bypassContainer.setVisibility(VISIBLE);
            }
            else
            {
                readonlyValueTextView.setVisibility(VISIBLE);
                readonlyValueTextView.setText(R.string.not_set);

                bypassContainer.setVisibility(GONE);
            }
        }
        else
        {
            readonlyValueTextView.setVisibility(GONE);

            bypassContainer.setVisibility(VISIBLE);

            Collection<InputField> collection = exclusionInputFieldsMap.values();
            List<InputField> fields = new ArrayList<InputField>(collection);
            if (fields.size() > 0)
            {
                if (TextUtils.isEmpty(fields.get(fields.size() - 1).getValue()))
                {
                    // DO NOTHING
                }
                else
                {
                    addEmptyItem();
                }
            }
            else
            {
                addEmptyItem();
            }

            updateExclusionStringValue();
        }

        App.getTraceUtils().stopTrace(TAG, "refreshExclusionList", Log.DEBUG);
    }

    private void addEmptyItem()
    {
        App.getTraceUtils().startTrace(TAG, "addEmptyItem", Log.DEBUG, true);

        InputField i = createExclusionInputField();
        i.setValue("");
        exclusionInputFieldsMap.put(i.getUUID(), i);
//        uiHandler.callRefreshExclusionList();

        App.getTraceUtils().stopTrace(TAG, "addEmptyItem", Log.DEBUG);
    }

    private class UIHandler extends Handler
    {
        private static final String REFRESH_UI_ACTION = "REFRESH_UI_ACTION";
        private static final String ADD_EMPTY_ITEM_ACTION = "ADD_EMPTY_ITEM_ACTION";
        private static final String REFRESH_EXCLUSION_LIST_ACTION = "REFRESH_EXCLUSION_LIST_ACTION";

        @Override
        public void handleMessage(Message message)
        {
            Bundle b = message.getData();

//            Timber.w("handleMessage: " + b.toString());

            if (b.containsKey(REFRESH_UI_ACTION))
                refreshUI();
            else if (b.containsKey(ADD_EMPTY_ITEM_ACTION))
                addEmptyItem();
            else if (b.containsKey(REFRESH_EXCLUSION_LIST_ACTION))
                refreshExclusionList();
        }

        public void callRefreshUI()
        {
            Message message = this.obtainMessage();
            Bundle b = new Bundle();
            b.putString(REFRESH_UI_ACTION, "");
            message.setData(b);
            sendMessageDelayed(message, 0);
        }

        public void callRefreshExclusionList()
        {
            Message message = this.obtainMessage();
            Bundle b = new Bundle();
            b.putString(REFRESH_EXCLUSION_LIST_ACTION, "");
            message.setData(b);
            sendMessageDelayed(message, 0);
        }

        public void callAddEmptyItem()
        {
            Message message = this.obtainMessage();
            Bundle b = new Bundle();
            b.putString(ADD_EMPTY_ITEM_ACTION, "");
            message.setData(b);
            sendMessageDelayed(message, 0);
        }
    }

    public class BypassTextWatcher implements TextWatcher
    {
        private final InputField inputField;

        public BypassTextWatcher(InputField field)
        {
            inputField = field;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count)
        {
            if (!readonly && inputField.enableTextListener)
            {
                if (start == 0 && before == 0 && count >= 1)
                {
                    uiHandler.callAddEmptyItem();
                    uiHandler.callRefreshExclusionList();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            updateExclusionStringValue();

            String value = editable.toString();
            inputField.setError(null);
            ProxyStatusItem item = ProxyUtils.isProxyValidExclusionAddress(value);
            if (!item.result)
            {
                inputField.setError(item.message);
            }
        }
    }

    private void updateExclusionStringValue()
    {
        String updatedExclusionString = getExclusionString();
        if (!exclusionString.equals(updatedExclusionString))
        {
            exclusionString = updatedExclusionString;
            sendOnValueChanged(exclusionString);
        }
    }

    public void addValueChangedListener(ValueChangedListener watcher)
    {
        if (mListeners == null)
        {
            mListeners = new ArrayList<ValueChangedListener>();
        }

        mListeners.add(watcher);
    }

    public interface ValueChangedListener
    {
        public void onExclusionListChanged(String result);
    }

    void sendOnValueChanged(String value)
    {
        if (mListeners != null)
        {
            final ArrayList<ValueChangedListener> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++)
            {
                list.get(i).onExclusionListChanged(value);
            }
        }
    }
}
