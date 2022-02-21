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

import android.content.Context;

import com.google.gson.Gson;
import com.guodong.sun.guodong.Config;
import com.guodong.sun.guodong.api.ApiHelper;
import com.guodong.sun.guodong.api.DuanZiApi;
import com.guodong.sun.guodong.entity.duanzi.NeiHanDuanZi;
import com.guodong.sun.guodong.presenter.IDuanziPresenter;
import com.guodong.sun.guodong.uitls.CacheUtil;
import com.guodong.sun.guodong.view.IDuanziView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/10.
 */

public class DuanziPreenterImpl extends BasePresenterImpl implements IDuanziPresenter
{

    private IDuanziView mDuanziView;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();
    private LifecycleTransformer bind;

    public DuanziPreenterImpl(Context context, IDuanziView mDuanziView, LifecycleTransformer bind)
    {
        this.mDuanziView = mDuanziView;
        mCacheUtil = CacheUtil.get(context);
        this.bind = bind;
    }

    @Override
    public void getDuanziData(int page)
    {
        mDuanziView.showProgressBar();
        Subscription subscription = ApiHelper
                .getInstance()
                .getApi(DuanZiApi.class, ApiHelper.DUANZI_BASE_URL)
                .getDuanZiData(page)
                .compose(bind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NeiHanDuanZi>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mDuanziView.hideProgressBar();
                        mDuanziView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(NeiHanDuanZi list)
                    {
                        mDuanziView.hideProgressBar();
                        mCacheUtil.put(Config.DUANZI, gson.toJson(list));
                        ArrayList<NeiHanDuanZi.Data> datas = new ArrayList<>();
                        for (NeiHanDuanZi.Data data : list.getData().getData())
                        {
                            if (data.getAd() == null)
                                datas.add(data);
                        }
                        mDuanziView.updateDuanziData(datas);
                    }
                });
        addSubscription(subscription);
    }
}