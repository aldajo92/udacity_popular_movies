package com.android.aldajo92.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.android.aldajo92.popularmovies.AppExecutors;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.models.MoviesReviewResponse;
import com.android.aldajo92.popularmovies.models.MoviesVideoResponse;
import com.android.aldajo92.popularmovies.newnetwork.MoviesAPI;
import com.android.aldajo92.popularmovies.newnetwork.MoviesService;

import retrofit2.Call;

public class DetailMovieViewModel extends AndroidViewModel {

    private MovieDatabase database;

    private MutableLiveData<Boolean> isMarked;

    private MoviesAPI moviesApi;

    public DetailMovieViewModel(Application application) {
        super(application);
        database = MovieDatabase.getInstance(this.getApplication());
        moviesApi = MoviesService.getClient().create(MoviesAPI.class);

        isMarked = new MutableLiveData<>();
    }

    public LiveData<FavoriteMovieEntry> getFavoriteMovie(long id) {
        return database.favoriteMovieDao().getFavoriteMovieById(id);
    }

    public Call<MoviesVideoResponse> requestMovies(long id) {
        return moviesApi.getVideos(id);
    }

    public Call<MoviesReviewResponse> requestReviews(long id) {
        return moviesApi.getReviews(id);
    }

    public MutableLiveData<Boolean> getIsMarked() {
        return isMarked;
    }

    public void saveFavoriteMovie(final FavoriteMovieEntry movieEntry) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.favoriteMovieDao().insertFavoriteMovie(movieEntry);
                isMarked.postValue(true);
            }
        });
    }

    public void removeFavoriteMovie(final FavoriteMovieEntry movieEntry) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.favoriteMovieDao().deleteFavoriteMovie(movieEntry);
                isMarked.postValue(false);
            }
        });
    }
}
