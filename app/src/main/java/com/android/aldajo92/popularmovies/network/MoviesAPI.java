package com.android.aldajo92.popularmovies.network;

import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.models.MoviesModelResponse;
import com.android.aldajo92.popularmovies.models.MoviesReviewResponse;
import com.android.aldajo92.popularmovies.models.MoviesVideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("/3/movie/{filter}")
    Call<MoviesModelResponse> getMovies(@Path("filter") String filter, @Query("page") int page);

    @GET("/3/movie/{movieId}/videos")
    Call<MoviesVideoResponse> getVideos(@Path("movieId") long id);

    @GET("/3/movie/{movieId}/reviews")
    Call<MoviesReviewResponse> getReviews(@Path("movieId") long id);

    @GET("/3/movie/{movieId}")
    Call<MovieModel> getMovie(@Path("movieId") long id);
}
