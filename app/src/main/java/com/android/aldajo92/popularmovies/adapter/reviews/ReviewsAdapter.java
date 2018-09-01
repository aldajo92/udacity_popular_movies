package com.android.aldajo92.popularmovies.adapter.reviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.adapter.detail.ItemVideoHolder;
import com.android.aldajo92.popularmovies.models.ReviewModel;
import com.android.aldajo92.popularmovies.models.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ItemReviewHolder> {

    private List<ReviewModel> list = new ArrayList<>();
    private ItemClickedListener<ReviewModel> listener;

    public ReviewsAdapter(ItemClickedListener<ReviewModel> listener) {
        this.listener = listener;
    }

    @Override
    public ItemReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_review, parent, false);
        return new ItemReviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ItemReviewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(ReviewModel movieModel) {
        list.add(movieModel);
        notifyDataSetChanged();
    }

    public void addItems(List<ReviewModel> movieModelList) {
        list = movieModelList;
        notifyDataSetChanged();
    }

}
