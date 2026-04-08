package com.example.myapplication.models;

public class Movie {
    private String movieId;
    private String title;
    private String description;
    private String posterUrl;
    private long duration; // Dùng long để khớp với kiểu number của Firestore
    private String genre;
    private double rating;

    public Movie() {}

    public Movie(String movieId, String title, String description, String posterUrl, long duration, String genre, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.genre = genre;
        this.rating = rating;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
