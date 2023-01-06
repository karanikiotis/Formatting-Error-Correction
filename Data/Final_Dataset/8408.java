/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.shop.cart.internal.support;

import java.util.List;

import org.mayocat.shop.taxes.PriceWithTaxes;

/**
 * @version $Id: bf639b10ed7cf107444b03384fc9d65f5b660fa1 $
 */
public class TestExpectation
{
    private List<ItemExpectation> items;

    private PriceWithTaxes itemsTotal;

    public List<ItemExpectation> getItems()
    {
        return items;
    }

    public PriceWithTaxes getItemsTotal()
    {
        return itemsTotal;
    }
}
