package com.android.aldajo92.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.fragments.CatalogMoviesFragment;
import com.android.aldajo92.popularmovies.fragments.FavoritesMoviesFragment;
import com.android.aldajo92.popularmovies.fragments.MoviesFragmentListener;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.viewmodel.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;
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

    int selectedFilterID = R.id.action_filter_popular;

    private MainViewModel viewModel;

    private AlertDialog alertDialog;

    private CatalogMoviesFragment catalogMoviesFragment;
    private FavoritesMoviesFragment favoritesMoviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = new MainViewModel(this.getApplication(), this);

        catalogMoviesFragment = CatalogMoviesFragment.getInstance(this);
        favoritesMoviesFragment = FavoritesMoviesFragment.getInstance(this);

        setFragment(catalogMoviesFragment);

        if (savedInstanceState == null) {
            createAlertDialog();
            initListeners();
            viewModel.getMovieList();
        }

//        viewModel.getFavoriteMovieEntries().observe(this, new Observer<List<FavoriteMovieEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {
//                favoritesMoviesFragment.setMovies(favoriteMovieEntries);
//            }
//        });
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
        //add in a base fragment
        catalogMoviesFragment.addItems(movies);
    }

    @Override
    public void clearList() {
        //add in a base fragment
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

    public void setFragment(Fragment fragment) {
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
                    //TODO improve this call
                    viewModel.setSelectedFilter(MOVIE_PARAM);
                    viewModel.getMovieList();
                    setCatalogFragment();
                    return true;
                case R.id.filter_top_rated:
                    //TODO improve this call
                    viewModel.setSelectedFilter(TOP_RATED_PARAM);
                    viewModel.getMovieList();
                    setCatalogFragment();
                    return true;
                case R.id.filter_favorites:
                    setFavoritesFragment();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setCatalogFragment(){
        setFragment(catalogMoviesFragment);
    }

    private void setFavoritesFragment() {
        setFragment(favoritesMoviesFragment);
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
}
