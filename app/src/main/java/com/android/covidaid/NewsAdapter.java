package com.android.covidaid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private List<Articles> articlesList;

    public NewsAdapter(Context context, List<Articles> articlesList) {
        this.context = context;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.article_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
        Source source = articlesList.get(position).getSource();
        holder.newsTitle.setText(articlesList.get(position).getTitle());
//        holder.newsDescription.setText(articlesList.get(position).getDescription());
        String date = articlesList.get(position).getPublishedAt();
        String YY = date.substring(0,4);
        String DD = date.substring(8,10);
        String MM= date.substring(5,7);

        holder.newsPublishedAt.setText(DD + "-"+ MM + "-"+YY);
        holder.newsSource.setText(source.getName());
        //holder.newsUrl.setText(articlesList.get(position).getUrl());

        Glide.with(context)
                .load(articlesList.get(position).getUrlToImage())
                .into(holder.newsImage);

//        holder.newsUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri url = Uri.parse(articlesList.get(position).getUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, url);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle, newsPublishedAt, newsDescription, newsSource, newsUrl;
        ImageView newsImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newTitle);
            newsPublishedAt = itemView.findViewById(R.id.newsPublishedAt);
//            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsSource = itemView.findViewById(R.id.sourceName);
            newsImage = itemView.findViewById(R.id.newsImage);
//            newsUrl = itemView.findViewById(R.id.urlToNews);
        }
    }
}