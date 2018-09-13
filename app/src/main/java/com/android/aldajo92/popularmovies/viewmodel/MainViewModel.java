package com.android.aldajo92.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.android.aldajo92.popularmovies.MainViewListener;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.models.MoviesModelResponse;
import com.android.aldajo92.popularmovies.network.MoviesAPI;
import com.android.aldajo92.popularmovies.network.MoviesService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.aldajo92.popularmovies.utils.Constants.MOVIE_PARAM;

public class MainViewModel extends AndroidViewModel {

    private MainViewListener listener;

    private LiveData<List<FavoriteMovieEntry>> favoriteMoviesEntries;

    private MoviesAPI moviesApi;

    private String selectedFilter = MOVIE_PARAM;

    public MainViewModel(@NonNull Application application, MainViewListener listener) {
        super(application);

        this.listener = listener;

        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        favoriteMoviesEntries = database.favoriteMovieDao().getFavoritesMovies();

        moviesApi = MoviesService.getClient().create(MoviesAPI.class);
    }

    public LiveData<List<FavoriteMovieEntry>> getFavoriteMovieEntries() {
        return favoriteMoviesEntries;
    }

    public void getMovieList() {
        getMoviesByPage(selectedFilter, 1);
    }

    public void getMoviesListByPage(int page){
        getMoviesByPage(selectedFilter, page);
    }

    private void getMoviesByPage(String filter, int page) {
        listener.showLoader();
        Call<MoviesModelResponse> call = moviesApi.getMovies(filter, page);
        call.enqueue(new Callback<MoviesModelResponse>() {
            @Override
            public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                listener.hideLoader();
                MoviesModelResponse moviesModelResponse = response.body();
                if (moviesModelResponse != null) {
                    listener.handleResponse(moviesModelResponse.getMovies());
                }
            }

            @Override
            public void onFailure(Call<MoviesModelResponse> call, Throwable t) {
                listener.hideLoader();
                listener.showNetworkError();
            }
        });
    }

    public void setSelectedFilter(String selectedFilter) {
        if(!this.selectedFilter.equals(selectedFilter)){
            this.selectedFilter = selectedFilter;
        }
    }

    public Call<MovieModel> requestMovie(long id) {
        return moviesApi.getMovie(id);
    }

    public void getFavoriteMovieList() {

    }
}
