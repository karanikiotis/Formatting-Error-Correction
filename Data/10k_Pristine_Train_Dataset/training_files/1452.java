/*
 * Copyright (C) 2014 Simon Vig Therkildsen
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

package net.simonvt.cathode.api.service;

import java.util.List;
import net.simonvt.cathode.api.entity.AnticipatedItem;
import net.simonvt.cathode.api.entity.Comment;
import net.simonvt.cathode.api.entity.People;
import net.simonvt.cathode.api.entity.Rating;
import net.simonvt.cathode.api.entity.Show;
import net.simonvt.cathode.api.entity.ShowProgress;
import net.simonvt.cathode.api.entity.TrendingItem;
import net.simonvt.cathode.api.entity.UpdatedItem;
import net.simonvt.cathode.api.enumeration.Extended;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShowsService {

  /**
   * Returns the most popular shows. Popularity is calculated using the rating percentage and the
   * number of ratings.
   */
  @GET("/shows/popular") Call<List<Show>> getPopularShows();

  /**
   * Returns all shows being watched right now. Shows with the most users are returned first.
   */
  @GET("/shows/trending") Call<List<TrendingItem>> getTrendingShows();

  /**
   * Returns all shows being watched right now. Shows with the most users are returned first.
   */
  @GET("/shows/trending") Call<List<TrendingItem>> getTrendingShows(@Query("limit") int limit);

  /**
   * Returns all shows being watched right now. Shows with the most users are returned first.
   */
  @GET("/shows/trending") Call<List<TrendingItem>> getTrendingShows(@Query("limit") int limit,
      @Query("extended") Extended extended);

  /**
   * Returns the most anticipated shows based on the number of lists a show appears on.
   */
  @GET("/shows/anticipated") Call<List<AnticipatedItem>> getAnticipatedShows();

  /**
   * Returns the most anticipated shows based on the number of lists a show appears on.
   */
  @GET("/shows/anticipated") Call<List<AnticipatedItem>> getAnticipatedShows(
      @Query("limit") int limit);

  /**
   * Returns the most anticipated shows based on the number of lists a show appears on.
   */
  @GET("/shows/anticipated") Call<List<AnticipatedItem>> getAnticipatedShows(
      @Query("limit") int limit, @Query("extended") Extended extended);

  /**
   * Returns all shows updated since the specified UTC date. We recommended storing the date you
   * can
   * be efficient using this method moving forward.
   *
   * @param startDate Updated since this date. Example: 2014-09-22.
   */
  @GET("/shows/updates/{start_date}") Call<List<UpdatedItem>> getUpdatedShows(
      @Path("start_date") String startDate, @Query("page") int page, @Query("limit") int limit);

  /**
   * Returns a single shows's details.
   *
   * @param id Trakt ID
   */
  @GET("/shows/{id}") Call<Show> getSummary(@Path("id") long id);

  /**
   * Returns a single shows's details.
   *
   * @param id Trakt ID
   */
  @GET("/shows/{id}") Call<Show> getSummary(@Path("id") long id,
      @Query("extended") Extended extended);

  /**
   * <b>OAuth required</b>
   * <p>
   * Returns collection progress for show including details on all seasons and episodes.
   * The next_episode will be the next episode the user should collect, if there are no upcoming
   * episodes it will be set to false.
   *
   * @param id Trakt ID
   */
  @GET("/shows/{id}/progress/collection") Call<ShowProgress> getCollectionProgress(
      @Path("id") long id);

  /**
   * <b>OAuth required</b>
   * <p>
   * Returns watched progress for show including details on all seasons and episodes.
   * The next_episode will be the next episode the user should watch, if there are no upcoming
   * episodes it will be set to false.
   *
   * @param id Trakt ID
   */
  @GET("/shows/{id}/progress/watched") Call<ShowProgress> getWatchedProgress(@Path("id") long id);

  /**
   * Returns all actors, directors, writers, and producers for a show.
   *
   * @param id Trakt ID
   */
  @GET("/shows/{id}/people") Call<People> getPeople(@Path("id") long id,
      @Query("extended") Extended extended);

  /**
   * Returns rating (between 0 and 10) and distribution for a show.
   *
   * @param id Trakt ID
   */
  @GET("/shows/{id}/ratings") Call<Rating> getRatings(@Path("id") long id);

  /**
   * <b>Pagination</b>
   * <p>
   * Returns all top level comments for a show. Most recent comments returned first.
   */
  @GET("/shows/{id}/comments") Call<List<Comment>> getComments(@Path("id") long id,
      @Query("page") int page, @Query("limit") int limit, @Query("extended") Extended extended);

  /**
   * <b>Pagination</b>
   * <p>
   * Returns related and similar shows.
   */
  @GET("/shows/{id}/related") Call<List<Show>> getRelated(@Path("id") long id,
      @Query("limit") int limit, @Query("extended") Extended extended);
}
