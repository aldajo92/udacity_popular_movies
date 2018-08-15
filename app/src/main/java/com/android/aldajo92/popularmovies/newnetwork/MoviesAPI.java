package com.android.aldajo92.popularmovies.newnetwork;

import com.android.aldajo92.popularmovies.models.MoviesModelResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesAPI {

    @GET("/3/movie/{filter}")
    Call<MoviesModelResponse> getMovies(@Path("filter") String filter);

}
