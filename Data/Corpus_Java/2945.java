package org.assertj.android.appcompat.v7.api.app;

import android.support.v7.app.ActionBar;
import org.assertj.core.api.AbstractAssert;

import static android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP;
import static android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM;
import static android.support.v7.app.ActionBar.DISPLAY_SHOW_HOME;
import static android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE;
import static android.support.v7.app.ActionBar.DISPLAY_USE_LOGO;
import static android.support.v7.app.ActionBar.NAVIGATION_MODE_LIST;
import static android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD;
import static android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS;
import static org.assertj.android.internal.IntegerUtils.buildBitMaskString;
import static org.assertj.android.internal.IntegerUtils.buildNamedValueString;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for {@link ActionBar} instances. */
public class ActionBarAssert extends AbstractAssert<ActionBarAssert, ActionBar> {
  public ActionBarAssert(ActionBar actual) {
    super(actual, ActionBarAssert.class);
  }

  public ActionBarAssert hasCustomView() {
    isNotNull();
    assertThat(actual.getCustomView()) //
        .overridingErrorMessage("Expected custom view but was not present.") //
        .isNotNull();
    return this;
  }

  public ActionBarAssert hasDisplayOptions(@ActionBarDisplayOptions int options) {
    isNotNull();
    final int actualOptions = actual.getDisplayOptions();
    //noinspection ResourceType
    assertThat(actualOptions) //
        .overridingErrorMessage("Expected display options <%s> but was <%s>.",
            displayOptionsToString(options), displayOptionsToString(actualOptions)) //
        .isEqualTo(options);
    return this;
  }

  public ActionBarAssert hasHeight(int height) {
    isNotNull();
    int actualHeight = actual.getHeight();
    assertThat(actualHeight) //
        .overridingErrorMessage("Expected height <%s> but was <%s>.", height, actualHeight) //
        .isEqualTo(height);
    return this;
  }

  public ActionBarAssert hasNavigationItemCount(int count) {
    isNotNull();
    int actualCount = actual.getNavigationItemCount();
    assertThat(actualCount) //
        .overridingErrorMessage("Expected count <%s> but was <%s>.", count, actualCount) //
        .isEqualTo(count);
    return this;
  }

  public ActionBarAssert hasNavigationMode(int mode) {
    isNotNull();
    int actualMode = actual.getNavigationMode();
    //noinspection ResourceType
    assertThat(actualMode) //
        .overridingErrorMessage("Expected mode <%s> but was <%s>.", navigationModeToString(mode),
            navigationModeToString(actualMode)) //
        .isEqualTo(mode);
    return this;
  }

  public ActionBarAssert hasSelectedNavigationIndex(int index) {
    isNotNull();
    int actualIndex = actual.getSelectedNavigationIndex();
    assertThat(actualIndex) //
        .overridingErrorMessage("Expected selected index <%s> but was <%s>.", index, actualIndex) //
        .isEqualTo(index);
    return this;
  }

  public ActionBarAssert hasSubtitle(CharSequence subtitle) {
    isNotNull();
    CharSequence actualSubtitle = actual.getSubtitle();
    assertThat(actualSubtitle) //
        .overridingErrorMessage("Expected subtitle <%s> but was <%s>.", subtitle, actualSubtitle) //
        .isEqualTo(subtitle);
    return this;
  }

  public ActionBarAssert hasSubtitle(int resId) {
    return hasSubtitle(actual.getThemedContext().getString(resId));
  }

  public ActionBarAssert hasTabCount(int count) {
    isNotNull();
    int actualCount = actual.getTabCount();
    assertThat(actualCount) //
        .overridingErrorMessage("Expected tab count of <%s> but was <%s>.", count, actualCount) //
        .isEqualTo(count);
    return this;
  }

  public ActionBarAssert hasTitle(CharSequence title) {
    isNotNull();
    CharSequence actualTitle = actual.getTitle();
    assertThat(actualTitle) //
        .overridingErrorMessage("Expected title <%s> but was <%s>.", title, actualTitle) //
        .isEqualTo(title);
    return this;
  }

  public ActionBarAssert hasTitle(int resId) {
    return hasTitle(actual.getThemedContext().getString(resId));
  }

  public ActionBarAssert isShowing() {
    isNotNull();
    assertThat(actual.isShowing()) //
        .overridingErrorMessage("Expected to be showing but was not showing.") //
        .isTrue();
    return this;
  }

  public ActionBarAssert isNotShowing() {
    isNotNull();
    assertThat(actual.isShowing()) //
        .overridingErrorMessage("Expected to be not showing but was showing.") //
        .isFalse();
    return this;
  }

  public static String navigationModeToString(@ActionBarNavigationMode int mode) {
    return buildNamedValueString(mode)
        .value(NAVIGATION_MODE_LIST, "list")
        .value(NAVIGATION_MODE_STANDARD, "standard")
        .value(NAVIGATION_MODE_TABS, "tabs")
        .get();
  }

  public static String displayOptionsToString(@ActionBarDisplayOptions int options) {
    return buildBitMaskString(options) //
        .flag(DISPLAY_HOME_AS_UP, "homeAsUp")
        .flag(DISPLAY_SHOW_CUSTOM, "showCustom")
        .flag(DISPLAY_SHOW_HOME, "showHome")
        .flag(DISPLAY_SHOW_TITLE, "showTitle")
        .flag(DISPLAY_USE_LOGO, "useLogo")
        .get();
  }
}
