package com.example.mymob2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mymob2.R;
import com.example.mymob2.Item;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Item> videos;
    private Context context;

    public VideoAdapter(List<Item> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Item video = videos.get(position);
        holder.titleTextView.setText(video.getSnippet().getTitle());
        holder.descriptionTextView.setText(video.getSnippet().getDescription());
        holder.channelTextView.setText("Channel: " + video.getSnippet().getChannelTitle());
        holder.publishTimeTextView.setText("Published: " + video.getSnippet().getPublishedAt().substring(0, 10));
        Glide.with(context)
                .load(video.getSnippet().getThumbnails().getMedium().getUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView, descriptionTextView, channelTextView, publishTimeTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            channelTextView = itemView.findViewById(R.id.channelTextView);
            publishTimeTextView = itemView.findViewById(R.id.publishTimeTextView);
        }
    }
}