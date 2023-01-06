/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.shop.shipping.model;

import java.math.BigDecimal;

/**
 * @version $Id: 21504499ccbc2f6c83ab952311ff0671a35dc7cf $
 */
public class CarrierRule
{
    private BigDecimal upToValue;

    private BigDecimal price;

    public BigDecimal getUpToValue()
    {
        return upToValue;
    }

    public void setUpToValue(BigDecimal upToValue)
    {
        this.upToValue = upToValue;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }
}
