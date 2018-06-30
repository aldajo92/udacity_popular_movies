package com.android.aldajo92.popularmovies.network;

public interface NetworkListener {

    void showLoader();

    void hideLoader();

    void onResponse(String response);

    void showNetworkError();
}
