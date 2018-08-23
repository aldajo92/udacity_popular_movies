package com.android.aldajo92.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.adapter.MovieItemListener;
import com.android.aldajo92.popularmovies.adapter.MoviesAdapter;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.models.MoviesModelResponse;
import com.android.aldajo92.popularmovies.newnetwork.MoviesAPI;
import com.android.aldajo92.popularmovies.newnetwork.MoviesService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.aldajo92.popularmovies.network.NetworkManager.MOVIE_PARAM;
import static com.android.aldajo92.popularmovies.network.NetworkManager.TOP_RATED_PARAM;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;

public class MainActivity extends AppCompatActivity implements MovieItemListener {

    private static String TAG = MainActivity.class.getName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.imageView_no_internet)
    ImageView imageViewNoInternet;

    @BindView(R.id.textView_no_internet)
    TextView textViewNoInternet;

    @BindView(R.id.button_try_again)
    TextView buttonTryAgain;

    MoviesAdapter adapter;
    AlertDialog alertDialog;

    List<MovieModel> movieModelList;

    int selectedFilterID = R.id.action_filter_popular;
    String selectedFilter = MOVIE_PARAM;

    private MovieDatabase mDb;

    private MoviesAPI moviesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            createAlertDialog();
            initRecyclerView();
            initListeners();
            moviesService = MoviesService.getClient().create(MoviesAPI.class);
            getNetworkData(MOVIE_PARAM);
        }

        mDb = MovieDatabase.getInstance(getApplicationContext());
        initDatabase();
    }

    private void initDatabase() {
        LiveData<List<FavoriteMovieEntry>> tasks = mDb.favoriteMovieDao().getFavoritesMovies();
        tasks.observe(this, new Observer<List<FavoriteMovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> taskEntries) {
//                Log.d(TAG, "Receiving database update from LiveData");
//                mAdapter.setTasks(taskEntries);
            }
        });
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.view_loader, null));
        alertDialog = builder.create();

        Window window = alertDialog.getWindow();
        if (window != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            window.setBackgroundDrawable(colorDrawable);
        }
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void initRecyclerView() {
        adapter = new MoviesAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }

    private void initListeners() {
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetworkData(selectedFilter);
            }
        });
    }

    private void getNetworkData(String filter) {
        showLoader();
        Call<MoviesModelResponse> call = moviesService.getMovies(filter);
        call.enqueue(new Callback<MoviesModelResponse>() {
            @Override
            public void onResponse(Call<MoviesModelResponse> call, Response<MoviesModelResponse> response) {
                hideLoader();
                MoviesModelResponse moviesModelResponse = response.body();
                if(moviesModelResponse != null){
                    handleResponse(moviesModelResponse.getMovies());
                }
            }

            @Override
            public void onFailure(Call<MoviesModelResponse> call, Throwable t) {
                hideLoader();
                showNetworkError();
            }
        });
    }

    private void showLoader() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    private void hideLoader() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void handleResponse(List<MovieModel> movies){
        movieModelList = movies;
        recyclerView.setVisibility(View.VISIBLE);
        textViewNoInternet.setVisibility(View.GONE);
        imageViewNoInternet.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);
        adapter.addItems(movieModelList);
    }

    private void showNetworkError() {
        recyclerView.setVisibility(View.GONE);
        textViewNoInternet.setVisibility(View.VISIBLE);
        imageViewNoInternet.setVisibility(View.VISIBLE);
        buttonTryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClicked(MovieModel movieModel, int position, ImageView imageView) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra(EXTRA_MOVIE_MODEL, movieModel);
        intent.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageView,
                ViewCompat.getTransitionName(imageView));

        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != selectedFilterID) {
            selectedFilterID = itemId;
            switch (itemId) {
                case R.id.action_filter_popular:
                    selectedFilter = MOVIE_PARAM;
                    getNetworkData(MOVIE_PARAM);
                    return true;
                case R.id.filter_top_rated:
                    selectedFilter = TOP_RATED_PARAM;
                    getNetworkData(TOP_RATED_PARAM);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
