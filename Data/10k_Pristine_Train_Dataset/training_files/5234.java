/*
 * Copyright (c) 2002-2012 Alibaba Group Holding Limited.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.sample.petstore.web.store.module.screen;

import static com.alibaba.sample.petstore.web.common.PetstoreConstant.*;

import javax.servlet.http.HttpSession;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.sample.petstore.biz.StoreManager;
import com.alibaba.sample.petstore.dal.dataobject.Cart;
import org.springframework.beans.factory.annotation.Autowired;

public class ViewCart {
    @Autowired
    private StoreManager storeManager;

    public void execute(HttpSession session, Context context) throws Exception {
        Cart cart = (Cart) session.getAttribute(PETSTORE_CART_KEY);

        if (cart == null) {
            cart = new Cart();
        }

        cart = storeManager.getCartItems(cart);

        context.put("cart", cart);
    }
}
