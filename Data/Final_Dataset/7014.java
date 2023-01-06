package com.lechucksoftware.proxy.proxysettings.ui.fragments;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.ui.adapters.PInfoAdapter;
import com.lechucksoftware.proxy.proxysettings.feedbackutils.PInfo;
import com.lechucksoftware.proxy.proxysettings.ui.base.BaseListFragment;
import com.lechucksoftware.proxy.proxysettings.loaders.PInfoTaskLoader;

import java.util.List;

public class ApplicationsFeedbackFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<List<PInfo>>
{
    public static final String TAG = ApplicationsFeedbackFragment.class.getSimpleName();
    private static ApplicationsFeedbackFragment instance;
    private TextView emptyText;

    public static final int LOADER_TEST = 1;
    private PInfoAdapter apListAdapter;
    ProgressDialog progressDialog;

    public static ApplicationsFeedbackFragment getInstance()
    {
        if (instance == null)
            instance = new ApplicationsFeedbackFragment();

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Toast.makeText(getActivity(), "CREATEVIEW", Toast.LENGTH_SHORT).show();

        View v = inflater.inflate(R.layout.applications_list, container, false);

        emptyText = (TextView) v.findViewById(android.R.id.empty);

        Loader<List<PInfo>> loader = getLoaderManager().initLoader(LOADER_TEST, new Bundle(), this);
        loader.forceLoad();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing... Please Wait...");
        progressDialog.show();

        return v;
    }

    @Override
    public Loader<List<PInfo>> onCreateLoader(int id, Bundle args)
    {
        return new PInfoTaskLoader(getActivity().getBaseContext());
    }

    @Override
    public void onLoadFinished(Loader<List<PInfo>> loader, List<PInfo> data)
    {
        apListAdapter = new PInfoAdapter(getActivity());
        setListAdapter(apListAdapter);
        apListAdapter.setData(data);

        progressDialog.dismiss();
//        Toast.makeText(getActivity(), "LOADED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<List<PInfo>> loader)
    {
//        Toast.makeText(getActivity(), "LOADRESET", Toast.LENGTH_SHORT).show();
    }
}