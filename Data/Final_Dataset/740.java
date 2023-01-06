package com.lechucksoftware.proxy.proxysettings.excluded;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lechucksoftware.proxy.proxysettings.App;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.constants.Intents;
import com.lechucksoftware.proxy.proxysettings.preferences.ValidationPreference;

import be.shouldit.proxy.lib.ProxyStatusItem;
import be.shouldit.proxy.lib.WiFiAPConfig;
import be.shouldit.proxy.lib.enums.CheckStatusValues;
import be.shouldit.proxy.lib.enums.ProxyStatusProperties;

public class ProxyCheckerPrefsFragment extends PreferenceFragment
{
    private ValidationPreference proxyEnabledPref;
    private ValidationPreference proxyReachablePref;
    private ValidationPreference proxyWebPref;
    private ValidationPreference proxyValidHostPref;
    private ValidationPreference proxyValidPortPref;
    private ValidationPreference wifiEnabledPref;
    private ValidationPreference wifiSelectedPref;
    private Preference startCheckPref;

    public static final String TAG = ProxyCheckerPrefsFragment.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.checker_preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        getUIComponents();
        refreshUIComponents();

        return v;
    }

    private void getUIComponents()
    {
        startCheckPref = (Preference) findPreference("preference_test_proxy_configuration");
        startCheckPref.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                App.getTraceUtils().d(TAG, "Calling broadcast intent " + Intents.PROXY_SETTINGS_MANUAL_REFRESH);
                getActivity().sendBroadcast(new Intent(Intents.PROXY_SETTINGS_MANUAL_REFRESH));
                return true;
            }
        });

        wifiEnabledPref = (ValidationPreference) findPreference("validation_wifi_enabled");
        wifiSelectedPref = (ValidationPreference) findPreference("validation_wifi_selected");
        proxyEnabledPref = (ValidationPreference) findPreference("validation_proxy_enabled");
        proxyValidHostPref = (ValidationPreference) findPreference("validation_proxy_valid_host");
        proxyValidPortPref = (ValidationPreference) findPreference("validation_proxy_valid_port");
        proxyReachablePref = (ValidationPreference) findPreference("validation_proxy_reachable");
        proxyWebPref = (ValidationPreference) findPreference("validation_web_reachable");
    }

    public void refreshUIComponents()
    {
        WiFiAPConfig conf = App.getWifiNetworksManager().getCachedConfiguration();

        if (conf.getStatus().getCheckingStatus() == CheckStatusValues.CHECKING)
        {
            startCheckPref.setEnabled(false);
            String checkedDate = conf.getStatus().getCheckedDateString();
            if (!TextUtils.isEmpty(checkedDate))
                startCheckPref.setSummary("Start checking on: " + checkedDate);
        }
        else
        {
            startCheckPref.setEnabled(true);
            String checkedDate = conf.getStatus().getCheckedDateString();
            if (checkedDate != null && checkedDate.length() > 0)
                startCheckPref.setSummary("Last checked on: " + checkedDate);
        }

        ProxyStatusItem wifi = conf.getStatus().getProperty(ProxyStatusProperties.WIFI_ENABLED);
        ProxyStatusItem wifiSelected = conf.getStatus().getProperty(ProxyStatusProperties.WIFI_SELECTED);
        ProxyStatusItem enabled = conf.getStatus().getProperty(ProxyStatusProperties.PROXY_ENABLED);
        ProxyStatusItem hostname = conf.getStatus().getProperty(ProxyStatusProperties.PROXY_VALID_HOSTNAME);
        ProxyStatusItem port = conf.getStatus().getProperty(ProxyStatusProperties.PROXY_VALID_PORT);
        ProxyStatusItem ping = conf.getStatus().getProperty(ProxyStatusProperties.PROXY_REACHABLE);
        ProxyStatusItem web = conf.getStatus().getProperty(ProxyStatusProperties.WEB_REACHABLE);

        checkProxyStatusItem(wifi, wifiEnabledPref);
        checkProxyStatusItem(wifiSelected, wifiSelectedPref);
        checkProxyStatusItem(enabled, proxyEnabledPref);
        checkProxyStatusItem(hostname, proxyValidHostPref);
        checkProxyStatusItem(port, proxyValidPortPref);
        checkProxyStatusItem(ping, proxyReachablePref);
        checkProxyStatusItem(web, proxyWebPref);
    }

    public void checkProxyStatusItem(ProxyStatusItem statusItem, ValidationPreference uiPref)
    {
        uiPref.SetStatus(statusItem);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ActionBar actionBar =  ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
