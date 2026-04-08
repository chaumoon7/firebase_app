package com.example.myapplication.models;

public class Seat {
    private String seatNumber;
    private boolean isBooked;
    private boolean isSelected;

    public Seat(String seatNumber, boolean isBooked) {
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
        this.isSelected = false;
    }

    public String getSeatNumber() { return seatNumber; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
