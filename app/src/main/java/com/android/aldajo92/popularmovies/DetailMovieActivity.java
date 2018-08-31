package com.android.aldajo92.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.utils.DateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.aldajo92.popularmovies.network.NetworkManager.IMAGE_HD_BASE_URL;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;

public class DetailMovieActivity extends AppCompatActivity {

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

    DetailMovieViewModel viewModel;

    MovieModel movieModel;

    FavoriteMovieEntry movieEntry;

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
                    if(isMarked){
                        movieEntry = favoriteMovieEntry;
                    }
                    updateMark();
                }
            });
            setupUI(movieModel);
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

    @OnClick(R.id.image_view_ic_star)
    public void onFavoriteClicked() {
        if (isMarked) {
            removeFavoriteMovie(movieEntry);
        } else {
            saveFavoriteMovie(new FavoriteMovieEntry(movieModel.getId(), movieModel.getName()));
        }
    }

    private void saveFavoriteMovie(final FavoriteMovieEntry movieEntry) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                viewModel.database.favoriteMovieDao().insertFavoriteMovie(movieEntry);
            }
        });
    }

    private void removeFavoriteMovie(final FavoriteMovieEntry movieEntry){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                viewModel.database.favoriteMovieDao().deleteFavoriteMovie(movieEntry);
                isMarked = false;
            }
        });
    }

    private void updateMark(){
        if (isMarked) {
            imageViewStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_marked));
        } else {
            imageViewStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
        }
    }

}
