package com.example.myapplication.models;

import java.util.Date;

public class Ticket {
    private String ticketId;
    private String userId;
    private String showtimeId;
    private String movieTitle; // Thêm tên phim để hiển thị nhanh
    private String seatNumber;
    private Date bookingDate;
    private double totalPrice;
    private String status; // e.g., "ĐÃ ĐẶT", "ĐÃ HỦY"

    public Ticket() {}

    public Ticket(String ticketId, String userId, String showtimeId, String movieTitle, String seatNumber, Date bookingDate, double totalPrice, String status) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.movieTitle = movieTitle;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
