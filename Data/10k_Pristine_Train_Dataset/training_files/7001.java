/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.shop.payment.model;

import java.util.Map;
import java.util.UUID;

import org.mayocat.model.Identifiable;

import com.google.common.collect.Maps;

/**
 * @version $Id: 54a418c155cb8a37bf5f08b65fbbf07f1ac3f015 $
 */
public class PaymentOperation implements Identifiable
{
    public enum Result
    {
        INITIALIZED,
        AUTHORIZED,
        CAPTURED,
        CANCELLED,
        REFUSED,
        FAILED,
        REFUND
    }

    private UUID id;

    private UUID orderId;

    private String gatewayId;

    private String externalId;

    private Result result;

    private Map<String, Object> memo = Maps.newHashMap();

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public UUID getOrderId()
    {
        return orderId;
    }

    public void setOrderId(UUID orderId)
    {
        this.orderId = orderId;
    }

    public String getGatewayId()
    {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId)
    {
        this.gatewayId = gatewayId;
    }

    public String getExternalId()
    {
        return externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

    public Map<String, Object> getMemo()
    {
        return memo;
    }

    public void setMemo(Map<String, Object> memo)
    {
        this.memo = memo;
    }
}
