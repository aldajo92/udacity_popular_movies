package com.android.aldajo92.popularmovies.adapter.reviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.ReviewModel;
import com.android.aldajo92.popularmovies.models.VideoModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.text_view_author)
    TextView textViewAuthor;

    @BindView(R.id.text_view_content)
    TextView textViewContent;

    private ReviewModel reviewModel;
    private ItemClickedListener<ReviewModel> listener;

    public ItemReviewHolder(View itemView, ItemClickedListener<ReviewModel> listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.itemClicked(reviewModel, getAdapterPosition(), itemView);
    }

    public void bindData(ReviewModel reviewModel) {
        this.reviewModel = reviewModel;
        textViewAuthor.setText(reviewModel.getAuthor());
        textViewContent.setText(reviewModel.getContent());
    }
}