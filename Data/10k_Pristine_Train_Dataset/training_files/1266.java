/*
 * Copyright (C) 2017 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of rebase-android
 *
 * rebase-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rebase-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rebase-android. If not, see <http://www.gnu.org/licenses/>.
 */

package com.drakeet.rebase;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import com.drakeet.rebase.api.RebaseRetrofit;
import com.drakeet.rebase.tool.Toasts;
import com.umeng.analytics.MobclickAgent;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.List;

import static com.drakeet.rebase.tool.ErrorHandlers.displayErrorConsumer;

/**
 * @author drakeet
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess(this)) {
            Stores.install(this);
            Toasts.install(this);
            RebaseRetrofit.setDebug(BuildConfig.DEBUG);
            MobclickAgent.setCatchUncaughtExceptions(true);
            MobclickAgent.setDebugMode(BuildConfig.DEBUG);
            RxJavaPlugins.setErrorHandler(displayErrorConsumer(this));
        }
    }


    public static boolean isMainProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfoList) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}