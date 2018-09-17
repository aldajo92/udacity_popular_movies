package com.android.aldajo92.popularmovies.adapter.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<ItemVideoHolder> {

    private List<VideoModel> list = new ArrayList<>();
    private ItemClickedListener<VideoModel> listener;

    public VideosAdapter(ItemClickedListener<VideoModel> listener) {
        this.listener = listener;
    }

    @Override
    public ItemVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_video, parent, false);
        return new ItemVideoHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ItemVideoHolder holder, int position) {
        holder.bindData(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(VideoModel movieModel) {
        list.add(movieModel);
        notifyDataSetChanged();
    }

    public void addItems(List<VideoModel> movieModelList) {
        list = movieModelList;
        notifyDataSetChanged();
    }

}
