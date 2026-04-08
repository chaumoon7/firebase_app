package com.example.myapplication.models;

import java.util.Date;

public class Showtime {
    private String showtimeId;
    private String movieId;
    private String theaterId;
    private Date startTime;
    private double price;

    public Showtime() {}

    public Showtime(String showtimeId, String movieId, String theaterId, Date startTime, double price) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.price = price;
    }

    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
