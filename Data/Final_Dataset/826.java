package com.lechucksoftware.proxy.proxysettings.ui.dialogs.likeapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lechucksoftware.proxy.proxysettings.App;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.ui.base.BaseDialogFragment;

public class LikeAppDialog extends BaseDialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(R.string.app_name);
        builder.content(R.string.do_you_like);

        builder.positiveText(R.string.yes);
        builder.negativeText(R.string.no);

        builder.callback(new MaterialDialog.ButtonCallback()
        {

            @Override
            public void onPositive(MaterialDialog dialog)
            {
                DoLikeAppDialog rateDialog = new DoLikeAppDialog();
                rateDialog.setCancelable(false);
                rateDialog.show(getFragmentManager(), "RateAppDialog");

                App.getEventsReporter().sendEvent(R.string.analytics_cat_dialogs_action,
                        R.string.analytics_act_like_dialog,
                        R.string.analytics_lab_like_app_dialog_yes, 0L);
            }

            @Override
            public void onNegative(MaterialDialog dialog)
            {
                DontLikeAppDialog feedbackDialog = new DontLikeAppDialog();
                feedbackDialog.setCancelable(false);
                feedbackDialog.show(getFragmentManager(), "MailFeedbackDialog");

                App.getEventsReporter().sendEvent(R.string.analytics_cat_dialogs_action,
                        R.string.analytics_act_like_dialog,
                        R.string.analytics_lab_like_app_dialog_no, 0L);
            }
        });

        MaterialDialog alert = builder.build();
        return alert;
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);

        App.getEventsReporter().sendEvent(R.string.analytics_cat_dialogs_action,
                R.string.analytics_act_like_dialog,
                R.string.analytics_lab_like_app_dialog_cancel, 0L);
    }
}
