package com.android.aldajo92.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.adapter.detail.VideosAdapter;
import com.android.aldajo92.popularmovies.adapter.reviews.ReviewsAdapter;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.models.MoviesReviewResponse;
import com.android.aldajo92.popularmovies.models.ReviewModel;
import com.android.aldajo92.popularmovies.models.VideoModel;
import com.android.aldajo92.popularmovies.viewmodel.DetailMovieViewModel;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.models.MoviesVideoResponse;
import com.android.aldajo92.popularmovies.utils.DateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.android.aldajo92.popularmovies.network.NetworkManager.IMAGE_HD_BASE_URL;
import static com.android.aldajo92.popularmovies.newnetwork.NetworkConstants.YOUTUBE_BASE_URL;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;

public class DetailMovieActivity extends AppCompatActivity implements ItemClickedListener<VideoModel> {

    @BindView(R.id.imageView_movie_picture)
    ImageView imageView;

    @BindView(R.id.image_view_ic_star)
    ImageView imageViewStar;

    @BindView(R.id.textView_movie_title)
    TextView textViewMovieTitle;

    @BindView(R.id.textView_title_original)
    TextView textViewTitleOriginal;

    @BindView(R.id.textView_overview)
    TextView textViewSummary;

    @BindView(R.id.textView_average)
    TextView textViewAverage;

    @BindView(R.id.textView_release_date)
    TextView textViewReleaseDate;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.recyclerView_reviews)
    RecyclerView recyclerViewReviews;

    private VideosAdapter videoAdapter;

    private ReviewsAdapter reviewsAdapter;

    private DetailMovieViewModel viewModel;

    private MovieModel movieModel;

    private FavoriteMovieEntry movieEntry;

    boolean isMarked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        viewModel = new DetailMovieViewModel(getApplication());
        supportPostponeEnterTransition();
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_MOVIE_MODEL) && intent.hasExtra(EXTRA_IMAGE_TRANSITION_NAME)) {
            movieModel = intent.getParcelableExtra(EXTRA_MOVIE_MODEL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String imageTransitionName = intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME);
                imageView.setTransitionName(imageTransitionName);
            }

            viewModel.getFavoriteMovie(movieModel.getId()).observe(this, new Observer<FavoriteMovieEntry>() {
                @Override
                public void onChanged(@Nullable FavoriteMovieEntry favoriteMovieEntry) {
                    isMarked = favoriteMovieEntry != null;
                    if (isMarked) {
                        movieEntry = favoriteMovieEntry;
                    }
                    updateMark();
                }
            });

            viewModel.getIsMarked().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean marked) {
                    isMarked = marked;
                    updateMark();
                }
            });

            viewModel.requestMovies(movieModel.getId()).enqueue(new retrofit2.Callback<MoviesVideoResponse>() {
                @Override
                public void onResponse(Call<MoviesVideoResponse> call, @NonNull Response<MoviesVideoResponse> response) {
                    MoviesVideoResponse moviesResponse = response.body();
                    if (moviesResponse != null) {
                        List<VideoModel> moviesVideoList = moviesResponse.getMovies();
                        videoAdapter.addItems(moviesVideoList);
                    }
                }

                @Override
                public void onFailure(Call<MoviesVideoResponse> call, Throwable t) {

                }
            });

            viewModel.requestReviews(movieModel.getId()).enqueue(new retrofit2.Callback<MoviesReviewResponse>() {
                @Override
                public void onResponse(Call<MoviesReviewResponse> call, Response<MoviesReviewResponse> response) {
                    MoviesReviewResponse reviewResponse = response.body();
                    if (reviewResponse != null) {
                        List<ReviewModel> reviewList = reviewResponse.getReviews();
                        reviewsAdapter.addItems(reviewList);
                    }
                }

                @Override
                public void onFailure(Call<MoviesReviewResponse> call, Throwable t) {
                    android.util.Log.i("ADJ", "");
                }
            });

            setupUI(movieModel);
            initRecyclerView();
        }
    }

    private void setupUI(MovieModel movieModel) {
        Picasso.get()
                .load(IMAGE_HD_BASE_URL + movieModel.getImageUrl())
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        textViewTitleOriginal.setText(movieModel.getName());
        textViewSummary.setText(movieModel.getOverview());
        textViewAverage.setText(movieModel.getAverage());
        String releaseDate = movieModel.getReleaseDate();
        String readableReleaseDate = DateUtils.changeDateFormat(
                releaseDate,
                DateUtils.MOVIE_DATE_FORMAT,
                DateUtils.READABLE_DATE_FORMAT
        );
        textViewReleaseDate.setText(readableReleaseDate);
    }

    private void initRecyclerView() {
        videoAdapter = new VideosAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(videoAdapter);

        reviewsAdapter = new ReviewsAdapter(new ItemClickedListener<ReviewModel>() {
            @Override
            public void itemClicked(ReviewModel data, int position, View imageView) {

            }
        });
        recyclerViewReviews.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );
        recyclerViewReviews.setAdapter(reviewsAdapter);


    }

    @OnClick(R.id.image_view_ic_star)
    public void onFavoriteClicked() {
        if (isMarked) {
            viewModel.removeFavoriteMovie(movieEntry);
        } else {
            viewModel.saveFavoriteMovie(new FavoriteMovieEntry(movieModel.getId(), movieModel.getName()));
        }
    }

    private void updateMark() {
        if (isMarked) {
            imageViewStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_marked));
        } else {
            imageViewStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
        }
    }

    @Override
    public void itemClicked(VideoModel data, int position, View imageView) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + data.getKey())));
    }
}
