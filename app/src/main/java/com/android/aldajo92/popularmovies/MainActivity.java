package com.android.aldajo92.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.fragments.CatalogMoviesFragment;
import com.android.aldajo92.popularmovies.fragments.FavoritesMoviesFragment;
import com.android.aldajo92.popularmovies.fragments.MoviesFragmentListener;
import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.viewmodel.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.android.aldajo92.popularmovies.utils.Constants.CURRENT_OPTION_SELECTED;
import static com.android.aldajo92.popularmovies.utils.Constants.CURRENT_SELECTED_ID;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_FAVORITE_MOVIE_MODEL;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;
import static com.android.aldajo92.popularmovies.utils.Constants.FAVORITES;
import static com.android.aldajo92.popularmovies.utils.Constants.MOVIE_PARAM;
import static com.android.aldajo92.popularmovies.utils.Constants.TOP_RATED_PARAM;

public class MainActivity extends AppCompatActivity implements MainViewListener, MoviesFragmentListener {

    @BindView(R.id.container)
    FrameLayout container;

    @BindView(R.id.imageView_no_internet)
    ImageView imageViewNoInternet;

    @BindView(R.id.textView_no_internet)
    TextView textViewNoInternet;

    @BindView(R.id.button_try_again)
    TextView buttonTryAgain;

    private
    int selectedFilterID = R.id.action_filter_popular;

    private MainViewModel viewModel;

    private AlertDialog alertDialog;

    private CatalogMoviesFragment catalogMoviesFragment;
    private FavoritesMoviesFragment favoritesMoviesFragment;

    private String optionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = new MainViewModel(this.getApplication(), this);

        catalogMoviesFragment = CatalogMoviesFragment.getInstance(this);
        favoritesMoviesFragment = FavoritesMoviesFragment.getInstance(this);

        viewModel.getMovieList();
        createAlertDialog();
        initListeners();

        if (savedInstanceState == null) {
            setFragmentByOptionSelected(MOVIE_PARAM);
        } else {
            setFragmentByOptionSelected(savedInstanceState.getString(CURRENT_OPTION_SELECTED, MOVIE_PARAM));
            selectedFilterID = savedInstanceState.getInt(CURRENT_SELECTED_ID, selectedFilterID);
        }
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

    private void initListeners() {
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getMovieList();
            }
        });
    }

    @Override
    public void showLoader() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    @Override
    public void hideLoader() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void handleResponse(List<MovieModel> movies) {
        textViewNoInternet.setVisibility(View.GONE);
        imageViewNoInternet.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        catalogMoviesFragment.addItems(movies);
    }

    @Override
    public void clearList() {
        catalogMoviesFragment.clearList();
    }

    @Override
    public void showNetworkError() {
        container.setVisibility(View.GONE);
        textViewNoInternet.setVisibility(View.VISIBLE);
        imageViewNoInternet.setVisibility(View.VISIBLE);
        buttonTryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setFragmentByOptionSelected(String optionSelected) {
        this.optionSelected = optionSelected;
        Fragment fragment;
        if (optionSelected.equals(FAVORITES)) {
            fragment = favoritesMoviesFragment;
        } else {
            fragment = catalogMoviesFragment;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != selectedFilterID) {
            selectedFilterID = itemId;
            switch (itemId) {
                case R.id.action_filter_popular:
                    setFragmentByOptionSelected(MOVIE_PARAM);
                    viewModel.setSelectedFilter(optionSelected);
                    viewModel.getMovieList();
                    return true;
                case R.id.filter_top_rated:
                    setFragmentByOptionSelected(TOP_RATED_PARAM);
                    viewModel.setSelectedFilter(optionSelected);
                    viewModel.getMovieList();
                    return true;
                case R.id.filter_favorites:
                    setFragmentByOptionSelected(FAVORITES);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void itemClicked(MovieModel movieModel, View view) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra(EXTRA_MOVIE_MODEL, movieModel);
        intent.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(view));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                ViewCompat.getTransitionName(view));

        startActivity(intent, options.toBundle());
    }

    @Override
    public void getMoviesListByPage(int page) {
        viewModel.getMoviesListByPage(page);
    }

    @Override
    public void favoriteMovieClicked(FavoriteMovieModel movieModel) {
        showLoader();
        viewModel.requestMovie(movieModel.getId()).enqueue(new retrofit2.Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                hideLoader();
                MovieModel movieModel = response.body();
                openDetailFromFavorite(movieModel);
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                showNetworkError();
            }
        });
    }

    private void openDetailFromFavorite(MovieModel movieModel) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra(EXTRA_FAVORITE_MOVIE_MODEL, movieModel);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_OPTION_SELECTED, optionSelected);
        outState.putInt(CURRENT_SELECTED_ID, selectedFilterID);
        super.onSaveInstanceState(outState);
    }
}
