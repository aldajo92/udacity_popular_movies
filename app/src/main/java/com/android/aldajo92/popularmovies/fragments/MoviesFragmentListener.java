package com.android.aldajo92.popularmovies.fragments;

import android.view.View;

import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;
import com.android.aldajo92.popularmovies.models.MovieModel;

public interface MoviesFragmentListener {
    void itemClicked(MovieModel movieModel, View view);

    void getMoviesListByPage(int page);

    void favoriteMovieClicked(FavoriteMovieModel data);
}
