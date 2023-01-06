package com.battlelancer.seriesguide.modules;

import android.content.Context;
import com.battlelancer.seriesguide.traktapi.SgTrakt;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.services.Checkin;
import com.uwetrottmann.trakt5.services.Comments;
import com.uwetrottmann.trakt5.services.Episodes;
import com.uwetrottmann.trakt5.services.Movies;
import com.uwetrottmann.trakt5.services.Recommendations;
import com.uwetrottmann.trakt5.services.Search;
import com.uwetrottmann.trakt5.services.Shows;
import com.uwetrottmann.trakt5.services.Sync;
import com.uwetrottmann.trakt5.services.Users;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Module
public class TraktModule {

    @Singleton
    @Provides
    Checkin provideCheckin(TraktV2 trakt) {
        return trakt.checkin();
    }

    @Singleton
    @Provides
    Comments provideComments(TraktV2 trakt) {
        return trakt.comments();
    }

    @Singleton
    @Provides
    Episodes provideEpisodes(TraktV2 trakt) {
        return trakt.episodes();
    }

    @Singleton
    @Provides
    Movies provideMovies(TraktV2 trakt) {
        return trakt.movies();
    }

    @Singleton
    @Provides
    Shows provideShows(TraktV2 trakt) {
        return trakt.shows();
    }

    @Singleton
    @Provides
    Recommendations provideRecommendations(TraktV2 trakt) {
        return trakt.recommendations();
    }

    @Singleton
    @Provides
    Search provideSearch(TraktV2 trakt) {
        return trakt.search();
    }

    @Singleton
    @Provides
    Sync provideSync(TraktV2 trakt) {
        return trakt.sync();
    }

    @Singleton
    @Provides
    Users provideUsers(TraktV2 trakt) {
        return trakt.users();
    }

    @Singleton
    @Provides
    TraktV2 provideTrakt(@ApplicationContext Context context, OkHttpClient okHttpClient) {
        return new SgTrakt(context, okHttpClient);
    }
}
