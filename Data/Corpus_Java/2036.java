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

import android.os.Bundle;
import android.os.ResultReceiver;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PinCodeActivityDelegateTests extends
        DigitsActivityDelegateTests<PinCodeActivityDelegate> {

    @Override
    public PinCodeActivityDelegate getDelegate() {
        return spy(new DummyPinCodeActivityDelegate(digitsEventCollector));
    }

    public void testIsValid() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertTrue(delegate.isValid(bundle));
    }

    public void testIsValid_missingResultReceiver() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingPhoneNumber() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

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

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingUserId() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingAuthStartTime() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withLanguage("lang")
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingLanguage() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withCountry("US");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingCountry() {
        final DigitsEventDetailsBuilder eventDetailsBuilder =
                new DigitsEventDetailsBuilder()
                        .withAuthStartTime(1L)
                        .withLanguage("lang");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putParcelable(DigitsClient.EXTRA_EVENT_DETAILS_BUILDER, eventDetailsBuilder);
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testGetLayoutId() {
        assertEquals(R.layout.dgts__activity_pin_code, delegate.getLayoutId());
    }

    public void testOnResume() {
        delegate.controller = controller;
        delegate.eventDetailsBuilder = new DigitsEventDetailsBuilder()
                .withLanguage("lang")
                .withCountry("US")
                .withAuthStartTime(1L);
        delegate.onResume();
        verify(controller).onResume();
        verify(digitsEventCollector).pinScreenImpression(detailsArgumentCaptor.capture());
        final DigitsEventDetails details = detailsArgumentCaptor.getValue();
        assertNotNull(details.language);
        assertNotNull(details.country);
        assertNotNull(details.elapsedTimeInMillis);
    }

    public class DummyPinCodeActivityDelegate extends PinCodeActivityDelegate {

        DummyPinCodeActivityDelegate(DigitsEventCollector digitsEventCollector) {
            super(digitsEventCollector);
        }
    }
}
