/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
 */
// based on Financisto

package org.totschnig.myexpenses.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.ContribInfoDialogActivity;
import org.totschnig.myexpenses.activity.ExpenseEdit;
import org.totschnig.myexpenses.activity.ManageTemplates;
import org.totschnig.myexpenses.model.Account;
import org.totschnig.myexpenses.model.ContribFeature;
import org.totschnig.myexpenses.model.Template;
import org.totschnig.myexpenses.model.Transaction;
import org.totschnig.myexpenses.model.Transfer;
import org.totschnig.myexpenses.preference.PrefKey;
import org.totschnig.myexpenses.provider.DatabaseConstants;
import org.totschnig.myexpenses.provider.TransactionProvider;
import org.totschnig.myexpenses.util.CurrencyFormatter;

import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_PLANID;

public class TemplateWidget extends AbstractWidget<Template> {

  private static final String WIDGET_INSTANCE_SAVE_ACTION = "org.totschnig.myexpenses.INSTANCE_SAVE";

  @Override
  Uri getContentUri() {
    return Uri
        .parse("content://org.totschnig.myexpenses/templatewidget");
  }

  @Override
  String getPrefName() {
    return "org.totschnig.myexpenses.activity.TemplateWidget";
  }

  @Override
  PrefKey getProtectionKey() {
    return PrefKey.PROTECTION_ENABLE_TEMPLATE_WIDGET;
  }

  public static final Uri[] OBSERVED_URIS = new Uri[]{
      TransactionProvider.TEMPLATES_URI,
      TransactionProvider.ACCOUNTS_URI //if color changes
  };

  private void addButtonsClick(Context context, RemoteViews updateViews,
                               int widgetId, long templateId) {
    Uri widgetUri = ContentUris.withAppendedId(getContentUri(), widgetId);
    Intent intent = new Intent(WIDGET_INSTANCE_SAVE_ACTION, widgetUri, context,
        TemplateWidget.class);
    intent.putExtra(WIDGET_ID, widgetId);
    intent.putExtra("ts", System.currentTimeMillis());
    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    updateViews.setOnClickPendingIntent(R.id.command1, pendingIntent);
    setImageViewVectorDrawable(context, updateViews, R.id.command1, R.drawable.ic_action_apply_save);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
      updateViews.setContentDescription(R.id.command1,
          context.getString(R.string.menu_create_instance_save));
    }
    intent = new Intent(context, ExpenseEdit.class);
    intent.putExtra(DatabaseConstants.KEY_TEMPLATEID, templateId);
    intent.putExtra(DatabaseConstants.KEY_INSTANCEID, -1L);
    intent.putExtra(AbstractWidget.EXTRA_START_FROM_WIDGET, true);
    intent.putExtra(AbstractWidget.EXTRA_START_FROM_WIDGET_DATA_ENTRY, true);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    pendingIntent = PendingIntent.getActivity(
        context,
        widgetId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
    updateViews.setOnClickPendingIntent(R.id.command2, pendingIntent);
    setImageViewVectorDrawable(context, updateViews, R.id.command2, R.drawable.ic_action_apply_edit);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
      updateViews.setContentDescription(R.id.command2,
          context.getString(R.string.menu_create_instance_edit));
    }
  }

  private void addTapOnClick(Context context, RemoteViews updateViews) {
    Intent intent = new Intent(context, ManageTemplates.class);
    intent.putExtra(AbstractWidget.EXTRA_START_FROM_WIDGET, true);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
    updateViews.setOnClickPendingIntent(R.id.object_info, pendingIntent);
  }

  @Override
  Template getObject(Cursor c) {
    return new Template(c);
  }

  @Override
  Cursor getCursor(Context c) {
    return c.getContentResolver().query(
        TransactionProvider.TEMPLATES_URI, null, KEY_PLANID + " is null", null, null);
  }

  @Override
  RemoteViews updateWidgetFrom(Context context, int widgetId, int layoutId,
                               Template t) {
    RemoteViews updateViews = new RemoteViews(context.getPackageName(),
        layoutId);
    updateViews.setTextViewText(R.id.line1,
        t.getTitle() + " : " + CurrencyFormatter.instance().formatCurrency(t.getAmount()));
    String commentSeparator = " / ";
    SpannableStringBuilder description = new SpannableStringBuilder(t.isTransfer() ?
        Transfer.getIndicatorPrefixForLabel(t.getAmount().getAmountMinor()) + t.label :
        t.label);
    if (!TextUtils.isEmpty(t.comment)) {
      if (description.length() != 0) {
        description.append(commentSeparator);
      }
      description.append(t.comment);
      int before = description.length();
      description.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), before, description.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    if (!TextUtils.isEmpty(t.payee)) {
      if (description.length() != 0) {
        description.append(commentSeparator);
      }
      description.append(t.payee);
      int before = description.length();
      description.setSpan(new UnderlineSpan(), before, description.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    updateViews.setTextViewText(R.id.note,
        description);
    setBackgroundColorSave(updateViews, R.id.divider3, Account.getInstanceFromDb(t.accountId).color);
    addScrollOnClick(context, updateViews, widgetId);
    addTapOnClick(context, updateViews);
    addButtonsClick(context, updateViews, widgetId, t.getId());
    saveForWidget(context, widgetId, t.getId());
    int multipleTemplatesVisible =
        Transaction.count(Template.CONTENT_URI, KEY_PLANID + " is null", null) < 2 ?
            View.GONE :
            View.VISIBLE;
    updateViews.setViewVisibility(R.id.navigation, multipleTemplatesVisible);
    return updateViews;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (WIDGET_INSTANCE_SAVE_ACTION.equals(action)) {
      if (MyApplication.getInstance().shouldLock(null)) {
        Toast.makeText(context,
            context.getString(R.string.warning_instantiate_template_from_widget_password_protected),
            Toast.LENGTH_LONG).show();
      } else {
        int widgetId = intent.getIntExtra(WIDGET_ID, INVALID_APPWIDGET_ID);
        if (widgetId != INVALID_APPWIDGET_ID) {
          long objectId = loadForWidget(context, widgetId);
          Transaction t = Transaction.getInstanceFromTemplate(objectId);
          if (t != null && t.save() != null) {
            Toast.makeText(context,
                context.getResources().getQuantityString(R.plurals.save_transaction_from_template_success, 1, 1),
                Toast.LENGTH_LONG).show();
            if (!ContribFeature.TEMPLATE_WIDGET.hasAccess()) {
              ContribFeature.TEMPLATE_WIDGET.recordUsage();
              showContribMessage(context);
            }
          }
        }
      }
    } else {
      super.onReceive(context, intent);
    }
  }

  @Override
  protected RemoteViews noDataUpdate(Context context) {
    RemoteViews updateViews = super.noDataUpdate(context);
    updateViews.setTextViewText(R.id.object_info, context.getString(R.string.no_templates));
    addTapOnClick(context, updateViews);
    return updateViews;
  }

  @Override
  public void onEnabled(Context context) {
    if (!ContribFeature.TEMPLATE_WIDGET.hasAccess()) {
      showContribMessage(context);
    }
    super.onEnabled(context);
  }

  public static void showContribMessage(Context context) {
    String message = ContribFeature.TEMPLATE_WIDGET.buildFullInfoString(context) + " " +
        ContribFeature.TEMPLATE_WIDGET.buildUsagesLefString(context);
    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    if (ContribFeature.TEMPLATE_WIDGET.usagesLeft() == 0) {
      updateWidgets(context, TemplateWidget.class);
    }
  }

  @Override
  protected void updateWidgets(Context context, AppWidgetManager manager,
                               int[] appWidgetIds, String action) {
    if (!isProtected() && !ContribFeature.TEMPLATE_WIDGET.hasAccess()) {
      int usagesLeft = ContribFeature.TEMPLATE_WIDGET.usagesLeft();
      if (usagesLeft < 1) {
        for (int id : appWidgetIds) {
          AppWidgetProviderInfo appWidgetInfo = manager.getAppWidgetInfo(id);
          if (appWidgetInfo != null) {
            String message = ContribFeature.TEMPLATE_WIDGET.buildFullInfoString(context) + " " +
                context.getString(R.string.dialog_contrib_no_usages_left);
            RemoteViews updateViews = errorUpdate(context, message);
            Intent intent = ContribInfoDialogActivity.getIntentFor(context, ContribFeature.TEMPLATE_WIDGET);
            updateViews.setOnClickPendingIntent(R.id.object_info,
                PendingIntent.getActivity(context, 0, intent, 0));
            manager.updateAppWidget(id, updateViews);
          }
        }
        return;
      }
    }
    super.updateWidgets(context, manager, appWidgetIds, action);
  }
}
