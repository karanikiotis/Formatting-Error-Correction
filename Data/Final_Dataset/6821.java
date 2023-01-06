/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.shop.payment;

/**
 * @version $Id: 64fbf8ba3f211244e3ac5ae8f269aad99430c1f5 $
 */
public enum BasePaymentData implements PaymentData
{
    GATEWAY,
    CURRENCY,
    CANCEL_URL,
    RETURN_URL,
    IPN_URL,
    BASE_WEB_URL,
    BASE_PLATFORM_URL,
    ORDER_ID,
    CUSTOMER,
    BILLING_ADDRESS,
    DELIVERY_ADDRESS,
    ORDER
}
