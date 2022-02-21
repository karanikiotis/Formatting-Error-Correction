/*
 * Copyright (C) 2017 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of rebase-android
 *
 * rebase-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rebase-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rebase-android. If not, see <http://www.gnu.org/licenses/>.
 */

package com.drakeet.rebase.api;

import com.drakeet.rebase.api.type.Auth;
import com.drakeet.rebase.api.type.Category;
import com.drakeet.rebase.api.type.Feed;
import io.reactivex.Flowable;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * <a href="https://github.com/drakeet/rebase-api">Rebase-API document</a>
 *
 * @author drakeet
 */
public interface Rebase {

    @GET("categories/{owner}")
    Flowable<List<Category>> categories(@Path("owner") String owner);

    @GET("categories/{owner}/{category}/feeds")
    Flowable<List<Feed>> feeds(
        @Path("owner") String owner,
        @Path("category") String category,
        @Query("last_id") String lastId,
        @Query("size") int size);

    @GET("authorizations/{username}")
    Flowable<Auth> login(@Path("username") String username, @Query("password") String password);

    @POST("categories/{username}/{category}/feeds")
    Flowable<Feed> newFeed(
        @Path("username") String username,
        @Path("category") String category, @Body Feed feed);
}
