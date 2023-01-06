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

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.SpannedString;
import android.view.View;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LoginCodeActivityDelegateTests extends
        DigitsActivityDelegateTests<LoginCodeActivityDelegate> {
    @Override
    public LoginCodeActivityDelegate getDelegate() {
        return spy(new DummyLoginCodeActivityDelegate(digitsEventCollector));
    }

    public void testIsValid() {
        final Bundle bundle = new Bundle();
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertTrue(delegate.isValid(bundle));
    }

    public void testIsValid_missingResultReceiver() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingPhoneNumber() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingRequestId() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingUserId() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingAuthStartTime() {
        final Bundle bundle = new Bundle();
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withLanguage("lang")
                        .withCountry("US");
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingLanguage() {
        final Bundle bundle = new Bundle();
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withCountry("US");
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingCountry() {
        final Bundle bundle = new Bundle();
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang");
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);

        assertFalse(delegate.isValid(bundle));
    }

    public void testGetLayoutId() {
        assertEquals(R.layout.dgts__activity_confirmation, delegate.getLayoutId());
    }

    @Override
    public void testSetUpTermsText() throws Exception {
        delegate.config = new AuthConfig();
        delegate.config.tosUpdate = Boolean.FALSE;
        delegate.tosFormatHelper = tosFormatHelper;
        doReturn(new SpannedString("")).when(tosFormatHelper).getFormattedTerms(anyInt());
        super.testSetUpTermsText();
        verify(tosFormatHelper).getFormattedTerms(R.string.dgts__terms_text_sign_in);
        verify(textView).setText(new SpannedString(""));
    }

    public void testSetUpTermsText_tosUpdated() throws Exception {
        delegate.tosFormatHelper = tosFormatHelper;
        doReturn(new SpannedString("")).when(tosFormatHelper)
                .getFormattedTerms(anyInt());
        delegate.config = new AuthConfig();
        delegate.config.tosUpdate = Boolean.TRUE;
        super.testSetUpTermsText();
        verify(tosFormatHelper).getFormattedTerms(eq(R.string.dgts__terms_text_updated));
        verify(textView).setText(new SpannedString(""));
    }

    @Override
    public void testSetUpEditText_nextAction() throws Exception {
        final Drawable dr = getContext().getResources()
                .getDrawable(Resources.getSystem()
                        .getIdentifier("indicator_input_error", "drawable", "android"));
        when(activity.getResources()).thenReturn(getContext().getResources());
        delegate.bucketedTextChangeListener = bucketedTextChangeListener;

        super.testSetUpEditText_nextAction();
        verify(editText).setText(getContext().getResources()
                .getString(R.string.dgts__confirmationEditTextPlaceholder));
        verify(editText).addTextChangedListener(bucketedTextChangeListener);
        verify(editText).setCompoundDrawablePadding(dr.getIntrinsicWidth() * -1);
    }

    @Override
    public void testSetUpEditText_noNextAction() throws Exception {
        when(activity.getResources()).thenReturn(getContext().getResources());
        delegate.bucketedTextChangeListener = bucketedTextChangeListener;
        super.testSetUpEditText_nextAction();
    }

    @Override
    public void testSetUpSendButton() throws Exception {
        super.testSetUpSendButton();
        verify(button).setStatesText(R.string.dgts__continue, R.string.dgts__sending,
                R.string.dgts__done);
        verify(button).showStart();
        verify(button).setEnabled(false);
    }

    public void testOnResume() {
        delegate.controller = controller;
        delegate.eventDetailsBuilder = new DigitsEventDetailsBuilder()
                .withLanguage("lang")
                .withCountry("US")
                .withAuthStartTime(1L);

        delegate.onResume();
        verify(controller).onResume();
        verify(digitsEventCollector).loginScreenImpression(detailsArgumentCaptor.capture());
        final DigitsEventDetails details = detailsArgumentCaptor.getValue();
        assertNotNull(details.language);
        assertNotNull(details.country);
        assertNotNull(details.elapsedTimeInMillis);
    }

    public void testSetupResendButton() throws Exception {
        delegate.setupResendButton(activity, controller, digitsEventCollector, resendButton);
        verify(resendButton).setEnabled(false);
        verify(resendButton).setOnClickListener(captorClick.capture());
        final View.OnClickListener listener = captorClick.getValue();
        listener.onClick(null);
        verify(digitsEventCollector).resendClickOnLoginScreen();
        verify(controller, atLeast(0)).clearError();
        verify(controller).resendCode(activity, resendButton, Verification.sms);
    }

    public void testSetupCallMeButton_voiceEnabled() throws Exception {
        final AuthConfig config = new AuthConfig();
        config.isVoiceEnabled = Boolean.TRUE;

        delegate.setupCallMeButton(activity, controller, digitsEventCollector,
                callMeButton, config);

        verify(callMeButton).setOnClickListener(captorClick.capture());
        final View.OnClickListener listener = captorClick.getValue();
        listener.onClick(null);
        verify(digitsEventCollector).callMeClickOnLoginScreen();
        verify(controller, atLeast(0)).clearError();
        verify(controller).resendCode(activity, callMeButton, Verification.voicecall);
        verify(callMeButton).setEnabled(false);
        verify(callMeButton).setVisibility(View.VISIBLE);
    }

    public void testSetupCallMeButton_voiceDisabled() throws Exception {
        final AuthConfig config = new AuthConfig();
        config.isVoiceEnabled = Boolean.FALSE;

        delegate.setupCallMeButton(activity, controller, digitsEventCollector,
                callMeButton, config);
        verify(callMeButton).setEnabled(false);
        verify(callMeButton).setVisibility(View.GONE);
    }

    public void testSetUpSmsIntercept_permissionDenied() {
        when(activity.checkCallingOrSelfPermission("android.permission.RECEIVE_SMS"))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        delegate.setUpSmsIntercept(activity, editText);

        verify(activity).checkCallingOrSelfPermission("android.permission.RECEIVE_SMS");
        verifyNoMoreInteractions(activity);
    }

    public void testSetUpSmsIntercept_permissionGranted() {
        when(activity.checkCallingOrSelfPermission("android.permission.RECEIVE_SMS"))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        delegate.setUpSmsIntercept(activity, editText);

        verify(activity).checkCallingOrSelfPermission("android.permission.RECEIVE_SMS");
        verify(activity).registerReceiver(any(SmsBroadcastReceiver.class), any(IntentFilter.class));
    }

    public class DummyLoginCodeActivityDelegate extends LoginCodeActivityDelegate {

        DummyLoginCodeActivityDelegate(DigitsEventCollector digitsEventCollector) {
            super(digitsEventCollector);
        }
    }
}
