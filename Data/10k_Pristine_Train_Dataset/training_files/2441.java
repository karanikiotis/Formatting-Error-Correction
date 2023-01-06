/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.digits.sdk.android;

import android.content.Context;
import android.os.ResultReceiver;
import android.text.TextWatcher;

/**
 * Interface that implements the logic business for
 * DigitsActivity
 */
interface DigitsController {
    void executeRequest(final Context context);

    void resendCode(final Context context, final InvertedStateButton resendButton,
                    final Verification verificationType);

    boolean validateInput(CharSequence text);

    void onResume();

    void startFallback(Context context, ResultReceiver receiver, DigitsException reason);

    TextWatcher getTextWatcher();

    ErrorCodes getErrors();

    int getErrorCount();

    void handleError(Context context, DigitsException digitsException);

    void clearError();

    void startTimer();

    void cancelTimer();
}
