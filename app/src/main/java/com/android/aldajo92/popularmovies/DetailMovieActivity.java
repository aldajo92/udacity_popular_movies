package com.android.aldajo92.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.models.MovieModel;
import com.android.aldajo92.popularmovies.utils.DateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.aldajo92.popularmovies.network.NetworkManager.IMAGE_HD_BASE_URL;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_IMAGE_TRANSITION_NAME;
import static com.android.aldajo92.popularmovies.utils.Constants.EXTRA_MOVIE_MODEL;

public class DetailMovieActivity extends AppCompatActivity {

    @BindView(R.id.imageView_movie_picture)
    ImageView imageView;

    @BindView(R.id.textView_movie_title)
    TextView textViewMovieTitle;

    @BindView(R.id.textView_overview)
    TextView textViewSummary;

    @BindView(R.id.textView_average)
    TextView textViewAverage;

    @BindView(R.id.textView_release_date)
    TextView textViewReleaseDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        supportPostponeEnterTransition();

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_MOVIE_MODEL) && intent.hasExtra(EXTRA_IMAGE_TRANSITION_NAME)) {
            MovieModel movieModel = intent.getParcelableExtra(EXTRA_MOVIE_MODEL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String imageTransitionName = intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME);
                imageView.setTransitionName(imageTransitionName);
            }

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

            textViewMovieTitle.setText(movieModel.getName());
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

    }
}
