package com.android.aldajo92.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.adapter.MoviesAdapter;
import com.android.aldajo92.popularmovies.adapter.PaginationMoviesScrollListener;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;
import static com.android.aldajo92.popularmovies.utils.Constants.MOVIE_PARAM;
import static com.android.aldajo92.popularmovies.utils.Constants.TOP_RATED_PARAM;

public class MainActivity extends AppCompatActivity implements ItemClickedListener<MovieModel>, MainViewListener {

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

    List<MovieModel> movieModelList = new ArrayList<>();

    int selectedFilterID = R.id.action_filter_popular;

    private MainViewModel viewModel;

    private PaginationMoviesScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = new MainViewModel(this.getApplication(), this);

        if (savedInstanceState == null) {
            createAlertDialog();
            initRecyclerView();
            initListeners();
            viewModel.getMovieList();
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

    private void initRecyclerView() {
        adapter = new MoviesAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new PaginationMoviesScrollListener(gridLayoutManager, 6) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount) {
                viewModel.getMoviesListByPage(currentPage + 1);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
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
        movieModelList.addAll(movies);
        recyclerView.setVisibility(View.VISIBLE);
        textViewNoInternet.setVisibility(View.GONE);
        imageViewNoInternet.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);
        adapter.addItems(movieModelList);
    }

    @Override
    public void clearList() {
        movieModelList.clear();
        scrollListener.resetPagination();
    }

    @Override
    public void showNetworkError() {
        recyclerView.setVisibility(View.GONE);
        textViewNoInternet.setVisibility(View.VISIBLE);
        imageViewNoInternet.setVisibility(View.VISIBLE);
        buttonTryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClicked(MovieModel movieModel, int position, View view) {
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
                    viewModel.setSelectedFilter(MOVIE_PARAM);
                    viewModel.getMovieList();
                    return true;
                case R.id.filter_top_rated:
                    viewModel.setSelectedFilter(TOP_RATED_PARAM);
                    viewModel.getMovieList();
                    return true;
                case R.id.filter_favorites:

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
