package com.android.aldajo92.popularmovies;

import com.android.aldajo92.popularmovies.models.MovieModel;

import java.util.List;

public interface MainViewListener {

    void showLoader();

    void hideLoader();

    void handleResponse(List<MovieModel> list);

    void clearList();

    void showNetworkError();
}
