package com.android.aldajo92.popularmovies.network.interfaces;

public interface ApiNetworkListener {
    void showLoader();

    void hideLoader();

    void onResponse(String response);

    void showNetworkError();
}