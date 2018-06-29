package com.android.aldajo92.popularmovies.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.aldajo92.popularmovies.network.NetworkManager.IMAGE_BASE_URL;

class ItemMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.textView_votes)
    TextView textViewVotes;

    @BindString(R.string.votes_average)
    String votesAverageTextFormat;

    @BindView(R.id.imageView_picture)
    ImageView imageViewPicture;

    private MovieModel model;
    private MovieItemListener listener;

    public ItemMovieHolder(View itemView, MovieItemListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.itemClicked(model, getAdapterPosition(), imageViewPicture);
    }

    public void bindData(MovieModel model) {
        this.model = model;

        String votesText = String.format(Locale.getDefault(), votesAverageTextFormat, model.getAverage());
        textViewVotes.setText(votesText);
        Picasso.get().load(IMAGE_BASE_URL + model.getImageUrl()).into(imageViewPicture);

        ViewCompat.setTransitionName(imageViewPicture, model.getName());
    }
}