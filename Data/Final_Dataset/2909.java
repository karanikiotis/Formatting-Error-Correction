package com.lechucksoftware.proxy.proxysettings.excluded;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lechucksoftware.proxy.proxysettings.App;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.ui.base.BaseFragment;

import be.shouldit.proxy.lib.APL;


/**
 * Created by marco on 21/05/13.
 */
public class StatusFragment extends BaseFragment
{
    private static final String TAG = StatusFragment.class.getSimpleName();
    public static StatusFragment instance;
    private Button statusButton;

    //    private Constants.StatusFragmentStates currentStatus;
    private StatusFragmentStates clickedStatus;
//    private View statusSpace;

    /**
     * Create a new instance of StatusFragment
     */
    public static StatusFragment getInstance()
    {
        if (instance == null)
            instance = new StatusFragment();

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.status, container, false);
        statusButton = (Button) view.findViewById(R.id.status_button);
//        statusSpace = view.findViewById(R.id.status_space);
//        statusButton.setBackgroundColor(getResources().getColor(R.color.Holo_Green_Light));
        return view;
    }

    public void setStatus(StatusFragmentStates status, String message)
    {
        setStatus(status, message, Boolean.FALSE);
    }

    public void setStatus(StatusFragmentStates status)
    {
        setStatus(status, null, Boolean.FALSE);
    }

    public void setStatus(StatusFragmentStates status, String message, Boolean isInProgress)
    {
        App.getTraceUtils().d(TAG, String.format("setStatus: %s ->  %s (%s)", clickedStatus, status.toString(), message));

        if (clickedStatus != null)
        {
            switch (clickedStatus)
            {
                case CONNECTED:
                    break;

                default:
                    if (status == clickedStatus)
                    {
                        App.getTraceUtils().d(TAG, String.format("already into status: %s", status.toString()));
                        return;
                    }
            }
        }

        switch (status)
        {
            case CONNECTED:
                clickedStatus = StatusFragmentStates.CONNECTED;
                setStatusInternal(message, null, R.drawable.btn_blue_holo_dark, true);
                break;
            case CHECKING:
                setStatusInternal(getResources().getString(R.string.checking_action), null, R.drawable.btn_blue_holo_dark, false);
                break;
            case CONNECT_TO:
                setStatusInternal(message, connectToWifi, R.drawable.btn_green_holo_dark, true);
                break;
            case NOT_AVAILABLE:
                setStatusInternal(message, null, R.drawable.btn_blue_holo_dark, false);
                break;
            case ENABLE_WIFI:
                setStatusInternal(getResources().getString(R.string.enable_wifi_action), enableWifi, R.drawable.btn_red_holo_dark, true);
                break;
            case GOTO_AVAILABLE_WIFI:
                setStatusInternal(message, configureNewWifiAp, R.drawable.btn_green_holo_dark, true);
                break;
            case NONE:
            default:
                hide();
        }
    }

    private void setStatusInternal(String status, View.OnClickListener listener, int resId, boolean enabled)
    {
//        LogWrapper.d(TAG,String.format("setStatusInternal to: %s",status.toString()));

        if (listener != null)
            statusButton.setText(String.format("%s...", status));
        else
            statusButton.setText(status);

        statusButton.setBackgroundResource(resId);
        statusButton.setEnabled(enabled);
        statusButton.setOnClickListener(listener);
        show();
    }

    View.OnClickListener enableWifi = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
//            hide();
            try
            {
                APL.enableWifi();
            }
            catch (Exception e)
            {
                App.getEventsReporter().sendException(new Exception("Exception during StatusFragment enableWifi action", e));
            }

            setStatus(StatusFragmentStates.CHECKING);
            clickedStatus = StatusFragmentStates.ENABLE_WIFI;
        }
    };

    View.OnClickListener connectToWifi = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
//            hide();

            try
            {
                //TODO : Fix this -> need to have the selected configuration
//                ProxyUtils.connectToAP(App.getSelectedConfiguration());
            }
            catch (Exception e)
            {
                App.getEventsReporter().sendException(new Exception("Exception during StatusFragment connectToWifi action", e));
            }

            setStatus(StatusFragmentStates.CHECKING);
            clickedStatus = StatusFragmentStates.CONNECT_TO;
        }
    };

    View.OnClickListener configureNewWifiAp = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
//            hide();
            Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            startActivity(intent);
            clickedStatus = StatusFragmentStates.CHECKING;
        }
    };

    public void hide()
    {
//        FragmentManager fm = this.getFragmentManager();
//        FragmentTransaction fts = fm.beginTransaction();
//        fts.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fts.hide(StatusFragment.getInstance());
//        fts.commit();

        statusButton.setVisibility(View.GONE);
//        statusSpace.setVisibility(View.GONE);
    }

    public void show()
    {
//        FragmentManager fm = this.getFragmentManager();
//        FragmentTransaction fts = fm.beginTransaction();
//        fts.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fts.show(StatusFragment.getInstance());
//        fts.commit();

        statusButton.setVisibility(View.VISIBLE);
//        statusSpace.setVisibility(View.VISIBLE);
    }
}
