package com.android.aldajo92.popularmovies.utils;

import com.android.aldajo92.popularmovies.BuildConfig;

public class Constants {
    public static final String API_KEY_MOVIE_DB = BuildConfig.API_KEY_THEMOVIE_DB;
    public static final String EXTRA_MOVIE_MODEL = "com.android.aldajo92.popularmovies.extraImage";
    public static final String EXTRA_FAVORITE_MOVIE_MODEL = "com.android.aldajo92.popularmovies.extraFavoriteMovie";
    public static final String EXTRA_IMAGE_TRANSITION_NAME = "com.android.aldajo92.popularmovies.transitionName";

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String IMAGE_HD_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public static final String MOVIE_PARAM = "popular";
    public static final String TOP_RATED_PARAM = "top_rated";
    public static final String FAVORITES = "favorites";

    public static final String CURRENT_OPTION_SELECTED = "com.android.aldajo92.popularmovies.currentOption";
    public static final String CURRENT_SELECTED_ID = "com.android.aldajo92.popularmovies.selectedId";
}
