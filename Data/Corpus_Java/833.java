package com.fangxu.dota2helper.ui.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fangxu.dota2helper.R;
import com.fangxu.dota2helper.callback.VideoStateCallback;
import com.fangxu.dota2helper.ui.widget.YoukuPluginPlayer;
import com.fangxu.dota2helper.util.BlurTransformation;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dear33 on 2016/7/14.
 */
public abstract class BaseVideoActivity extends BaseActivity implements VideoStateCallback {
    private static final String TAG = "BaseVideoActivity";
    public static final String VIDEO_BACKGROUND = "video_background";
    public static final String VIDEO_YOUKU_VID = "video_youku_vid";

    @Bind(R.id.iv_blur)
    ImageView mBlurImageView;
    @Bind(R.id.youku_player)
    YoukuPlayerView mYoukuPlayerView;
    @Bind(R.id.rl_blur_container)
    RelativeLayout mBlurImageContainer;

    protected String mVid = null;
    protected String mBackgroundUrl;

    protected YoukuBasePlayerManager mYoukuBasePlayerManager;
    protected YoukuPluginPlayer mPluginPlayer;
    protected YoukuPlayer mYoukuPlayer;
    protected boolean mIsPlayerReady = false;

    protected int mCurrentPlayTimeMills;
    protected int mVideoDurationMillis;
    protected boolean mIsVideoStarted = false;
    protected boolean mIsVideoEnded = false;

    protected void cacheWatchedVideo() {

    }

    protected void autoPlay() {

    }

    protected boolean hasToolbar() {
        return true;
    }

    @Override
    public boolean applySystemBarDrawable() {
        return false;
    }

    @Override
    protected boolean applyTranslucentStatus() {
        return false;
    }

    @Override
    public int getTitleResId() {
        return R.string.video_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (hasToolbar()) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        mVid = getIntent().getStringExtra(VIDEO_YOUKU_VID);
        initPlayer();
    }

    private void initPlayer() {
        mBackgroundUrl = getIntent().getStringExtra(VIDEO_BACKGROUND);
        Glide.with(this).load(mBackgroundUrl).asBitmap().placeholder(R.color.black).transform(new BlurTransformation(this)).into(mBlurImageView);
        mYoukuBasePlayerManager = new YoukuBasePlayerManager(this) {
            @Override
            public void setPadHorizontalLayout() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                // TODO Auto-generated method stub
                mPluginPlayer = new YoukuPluginPlayer(this, mediaPlayerDelegate, mBackgroundUrl);
                mPluginPlayer.setVideoStateCallback(BaseVideoActivity.this);
                addPlugins(mPluginPlayer);
                mYoukuPlayer = player;
                mIsPlayerReady = true;
                autoPlay();
            }

            @Override
            public void onSmallscreenListener() {
                // TODO Auto-generated method stub
                if (hasToolbar()) {
                    mToolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFullscreenListener() {
                // TODO Auto-generated method stub
                if (hasToolbar()) {
                    mToolbar.setVisibility(View.GONE);
                }
            }
        };
        mYoukuBasePlayerManager.onCreate();

        mYoukuPlayerView.setSmallScreenLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mYoukuPlayerView.setFullScreenLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mYoukuPlayerView.initialize(mYoukuBasePlayerManager);
    }

    @Override
    public void onVideoStart(int durationMillis) {
        Log.i(TAG, "onVideoStart, durationMillis=" + durationMillis);
        mVideoDurationMillis = durationMillis;
        mIsVideoStarted = true;
        mIsVideoEnded = false;
    }

    @Override
    public void onVideoEnd() {
        Log.i(TAG, "onVideoEnd");
        mIsVideoStarted = false;
        mIsVideoEnded = true;
    }

    @Override
    public void onProgressChanged(int currentTimeMillis) {
        Log.i(TAG, "currentTimeMillis=" + currentTimeMillis);
        mCurrentPlayTimeMills = currentTimeMillis;
    }

    @OnClick(R.id.iv_play)
    public void onClickPlay(ImageView imageView) {
        if (mIsPlayerReady && mVid != null) {
            mYoukuPlayer.playVideo(mVid);
            mBlurImageContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mYoukuBasePlayerManager.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mYoukuBasePlayerManager.onPause();
        cacheWatchedVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mYoukuBasePlayerManager.onResume();
    }

    @Override
    public boolean onSearchRequested() {
        return mYoukuBasePlayerManager.onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mYoukuBasePlayerManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mYoukuBasePlayerManager.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYoukuBasePlayerManager.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mYoukuBasePlayerManager.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBlurImageContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mBlurImageContainer.requestLayout();
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBlurImageContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.video_player_height)));
            mBlurImageContainer.requestLayout();
        }
    }

    @Override
    protected void onDestroy() {
        mYoukuBasePlayerManager.onDestroy();
        super.onDestroy();
    }
}
