package com.android.aldajo92.popularmovies.network;

import retrofit2.http.GET;

public interface MoviesImageAPI {

    @GET("/t/p/w185")
    String getLowResolutionImage();

    @GET("/t/p/w500")
    String geImage();

}
