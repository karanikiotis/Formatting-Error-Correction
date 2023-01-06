/*
 * Copyright (C) 2017 guodongAndroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-05-04 15:05:15
 *
 * GitHub:   https://github.com/guodongAndroid
 * Website:  http://www.sunxiaoduo.com
 * Email:    sun33919135@gmail.com
 * QQ:       33919135
 */

package com.guodong.sun.guodong.presenter.presenterImpl;

import com.guodong.sun.guodong.base.IBasePresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/10/9.
 */
public class BasePresenterImpl implements IBasePresenter
{

    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription s)
    {
        if (this.mCompositeSubscription == null)
            this.mCompositeSubscription = new CompositeSubscription();
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void unsubcrible()
    {
        if (this.mCompositeSubscription != null)
            this.mCompositeSubscription.unsubscribe();
    }
}
