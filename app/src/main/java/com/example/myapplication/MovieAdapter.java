package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.models.Movie;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvGenre.setText(movie.getGenre());
        holder.tvRating.setText("Rating: " + movie.getRating());
        
        // Sử dụng Glide để tải ảnh từ URL trên Firebase
        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(movie.getPosterUrl())
                .placeholder(android.R.drawable.ic_menu_gallery) // Ảnh hiển thị khi đang tải
                .error(android.R.drawable.stat_notify_error)      // Ảnh hiển thị khi lỗi link
                .into(holder.ivPoster);
        } else {
            holder.ivPoster.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvGenre, tvRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
