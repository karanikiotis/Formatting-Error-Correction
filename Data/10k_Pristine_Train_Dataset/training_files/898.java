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

package com.drakeet.rebase.tool;

import android.content.Context;
import android.util.Log;

/**
 * @author drakeet
 */
public class BlackBoxes {

    private static final String TAG = BlackBoxes.class.getSimpleName();
    public static final String ALIAS = "rebase";


    public static BlackBox newRebaseBox(Context context) {
        return new BlackBox(context, ALIAS);
    }


    public static boolean createKeys(BlackBox box) {
        try {
            box.createKeys();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "[createKeys]", e);
        }
        return false;
    }


    public static String encrypt(BlackBox box, String secret) {
        try {
            return box.encrypt(secret);
        } catch (Exception e) {
            Log.e(TAG, "[encrypt]", e);
        }
        return null;
    }


    public static String encrypt(Context context, String secret) {
        BlackBox box = BlackBoxes.newRebaseBox(context);
        BlackBoxes.createKeys(box);
        return encrypt(box, secret);
    }


    public static String decrypt(BlackBox box, String encrypted) {
        try {
            return box.decrypt(encrypted);
        } catch (Exception e) {
            Log.e(TAG, "[decrypt]", e);
        }
        return null;
    }


    public static String decrypt(Context context, String encrypted) {
        return decrypt(BlackBoxes.newRebaseBox(context), encrypted);
    }
}
