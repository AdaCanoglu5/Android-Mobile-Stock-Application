package com.sutock2;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        holder.tvSummary.setText(news.getSummary());
        holder.tvUrl.setText(news.getUrl());

        // Make URL clickable
        holder.tvUrl.setOnClickListener(v -> {
            Uri webpage = Uri.parse(news.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(holder.itemView.getContext().getPackageManager()) != null) {
                holder.itemView.getContext().startActivity(intent);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(news.getBannerImage())
                .into(holder.ivBannerImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSummary, tvUrl;
        ImageView ivBannerImage;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvSummary = view.findViewById(R.id.tvSummary);
            ivBannerImage = view.findViewById(R.id.ivBannerImage);
            tvUrl = view.findViewById(R.id.tvUrl);
            // Initialize other views
        }
    }
}
