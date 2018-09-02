package com.android.aldajo92.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.aldajo92.popularmovies.MainViewListener;
import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.models.MoviesModelResponse;
import com.android.aldajo92.popularmovies.newnetwork.MoviesAPI;
import com.android.aldajo92.popularmovies.newnetwork.MoviesService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.aldajo92.popularmovies.network.NetworkManager.MOVIE_PARAM;

public class MainViewModel extends AndroidViewModel {

    private MainViewListener listener;

    private LiveData<List<FavoriteMovieEntry>> tasks;

    private LiveData<List<MovieModel>> listLiveDataMovies;

    private MoviesAPI moviesApi;

    private MovieDatabase mDb;

    private String selectedFilter = MOVIE_PARAM;

    public MainViewModel(@NonNull Application application, MainViewListener listener) {
        super(application);

        this.listener = listener;

        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        tasks = database.favoriteMovieDao().getFavoritesMovies();

        listLiveDataMovies = new MutableLiveData<>();

        mDb = MovieDatabase.getInstance(application);
        moviesApi = MoviesService.getClient().create(MoviesAPI.class);
    }

    public LiveData<List<FavoriteMovieEntry>> getFavoritesMoviesFromDb() {
        return mDb.favoriteMovieDao().getFavoritesMovies();
    }

    public LiveData<List<FavoriteMovieEntry>> getTasks() {
        return tasks;
    }

    public Call<MoviesModelResponse> getMovies(String filter) {
        return moviesApi.getMovies(filter, 1);
    }

    public Call<MoviesModelResponse> getMoviesByPagination(String filter, int page) {
        return moviesApi.getMovies(filter, page);
    }

    public void getMovieList() {
        listener.clearList();
        getMoviesByPage(selectedFilter, 1);
    }

    public void getMoviesListByPage(int page){
        getMoviesByPage(selectedFilter, page);
    }

    public void getMoviesByPage(String filter, int page) {
        listener.showLoader();
        Call<MoviesModelResponse> call = getMoviesByPagination(filter, page);
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
        this.selectedFilter = selectedFilter;
    }
}
