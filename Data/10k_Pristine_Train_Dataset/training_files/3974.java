/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.ene.lab.io_timer.ui;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.ene.lab.io_timer.R;
import im.ene.lab.io_timer.ui.widget.ForegroundLinearLayout;
import im.ene.lab.io_timer.ui.widget.GoogleIO2016NumberView;
import im.ene.lab.io_timer.ui.widget.MiniDrawerLayout;
import im.ene.lab.io_timer.util.Number;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.threeten.bp.ZonedDateTime;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.logo) ImageView mLogo;
  @Bind(R.id.io_animation) AppCompatImageView mCrafty;  // in the name of the creator

  /**
   * This is either AnimatedVectorDrawable (on Lollipop and up) or AnimatedVectorDrawableCompat, but
   * I just care of it as a Animatable implementation.
   */
  private Animatable mCraftyDrawable;
  @Bind(R.id.side_logo) ImageView mHeader;
  @Bind(R.id.drawer) MiniDrawerLayout mDrawer;
  @Bind(R.id.extra_container) View mExtraViews;
  @Bind(R.id.settings) ForegroundLinearLayout mSetting;

  @OnClick(R.id.side_logo) void openCloseDrawer() {
    if (mDrawer != null) {
      if (mDrawer.isDrawerOpen()) {
        mDrawer.closeDrawer();
      } else {
        mDrawer.openDrawer();
      }
    }
  }

  @OnClick(R.id.settings) void openWebsite() {
    String url = "https://events.google.com/io2016/";
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  @OnClick(R.id.credits) void openCredits() {
    new AlertDialog.Builder(this).setTitle("About resources used in IO When")
        .setView(R.layout.credits_container)
        .create()
        .show();
  }

  @Bind({
      R.id.digit_0, R.id.digit_1, R.id.digit_2, R.id.digit_3, R.id.digit_4, R.id.digit_5,
      R.id.digit_6, R.id.digit_7
  }) List<GoogleIO2016NumberView> mDigitViews;

  @BindDimen(R.dimen.padding_8dp) int sideBarPadding;
  @BindDimen(R.dimen.drawer_collapse_size) int collapseSize;
  @BindDimen(R.dimen.drawer_expand_size) int expandSize;

  private Subscription mTimeSetupSubscription;
  private Integer[] mDigits = new Integer[8];
  private long mEventSecond;

  private int iconMaxSize;
  private int iconMinSize;

  private final Interpolator interpolator = new AccelerateInterpolator();

  static {
    // This is my expected mode in the future, so skip it for now.
    // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // get AnimatedVectorDrawable
    mCraftyDrawable = (Animatable) mCrafty.getDrawable();

    // Update date time then update UI.
    mEventSecond = ZonedDateTime.parse(getString(R.string.io_event_date)).toEpochSecond();

    iconMinSize = collapseSize - 2 * sideBarPadding;
    iconMaxSize = expandSize - 2 * sideBarPadding;

    ViewCompat.setPivotX(mHeader, 0);
    ViewCompat.setPivotY(mHeader, 0);

    mDrawerListener = new DrawerLayout.SimpleDrawerListener() {
      @Override public void onDrawerSlide(View drawerView, float value) {
        float scale = (iconMaxSize * value + iconMinSize * (1.f - value)) / iconMaxSize;
        if (mHeader != null) {
          ViewCompat.setScaleX(mHeader, scale);
          ViewCompat.setScaleY(mHeader, scale);
          mHeader.setAlpha(1 - scale);
        }

        if (mCrafty != null) {
          mCrafty.setAlpha(value);
        }

        if (mExtraViews != null) {
          mExtraViews.setAlpha(interpolator.getInterpolation(value));
        }
      }

      @Override public void onDrawerClosed(View drawerView) {
        if (mSetting != null) {
          mSetting.setClickable(false);
        }

        if (mCraftyDrawable != null && !mCraftyDrawable.isRunning()) {
          mCraftyDrawable.stop();
        }
      }

      @Override public void onDrawerOpened(View drawerView) {
        if (mSetting != null) {
          mSetting.setClickable(true);
        }

        if (mCraftyDrawable != null && !mCraftyDrawable.isRunning()) {
          mCraftyDrawable.start();
        }
      }
    };

    mDrawer.setDrawerListener(mDrawerListener);
    mDrawerListener.onDrawerSlide(mDrawer, 0.f);  // bad practice
  }

  private static final String TAG = "MainActivity";

  private DrawerLayout.DrawerListener mDrawerListener;

  @Override protected void onResume() {
    super.onResume();
    mTimeSetupSubscription = Observable.interval(1, TimeUnit.SECONDS)
        .map(new Func1<Long, Void>() {
          @Override public Void call(Long aLong) {
            if (mDigits == null) {
              mDigits = new Integer[8];
            }
            // Parse date-time
            long now = ZonedDateTime.now().toEpochSecond();
            long diff = mEventSecond - now;
            if (diff <= 0) {
              Arrays.fill(mDigits, 0);
            } else {
              // diff: second
              // diff / 60 minutes
              int dayLeft = (int) (diff / (60 * 60 * 24));
              mDigits[0] = dayLeft / 10;
              mDigits[1] = dayLeft % 10;
              diff -= dayLeft * (60 * 60 * 24);
              int hours = (int) (diff / (60 * 60));
              mDigits[2] = hours / 10;
              mDigits[3] = hours % 10;
              diff -= hours * (60 * 60);
              int minutes = (int) (diff / 60);
              mDigits[4] = minutes / 10;
              mDigits[5] = minutes % 10;
              diff -= minutes * 60;
              int seconds = (int) diff;
              mDigits[6] = seconds / 10;
              mDigits[7] = seconds % 10;
            }

            return null;
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Void>() {
          @Override public void call(Void param) {
            activeCountDown();
            mLogo.setVisibility(View.GONE);
          }
        });
  }

  private void activeCountDown() {
    for (int i = 0, count = Math.min(mDigits.length, mDigitViews.size()); i < count; i++) {
      mDigitViews.get(i).animateTo(Number.VALUES[mDigits[i]]);
    }
  }

  @Override protected void onPause() {
    if (mTimeSetupSubscription != null && !mTimeSetupSubscription.isUnsubscribed()) {
      mTimeSetupSubscription.unsubscribe();
    }
    super.onPause();
  }

  @Override protected void onDestroy() {
    mDrawer.setDrawerListener(null);
    mDrawerListener = null;
    super.onDestroy();
  }
}
