package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.models.Seat;
import com.example.myapplication.models.Showtime;
import com.example.myapplication.models.Ticket;
import com.google.firebase.firestore.DocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {
    private TextView tvBookingTitle, tvSelectedSeat;
    private Spinner spShowtimes;
    private RecyclerView rvSeats;
    private Button btnConfirm;
    private FirebaseRepository repository;
    private String movieId, movieTitle;
    private List<Showtime> showtimeList = new ArrayList<>();
    private List<String> showtimeLabels = new ArrayList<>();
    private List<Seat> seatList = new ArrayList<>();
    private SeatAdapter seatAdapter;
    private String selectedSeatNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        repository = new FirebaseRepository();
        movieId = getIntent().getStringExtra("movie_id");
        movieTitle = getIntent().getStringExtra("movie_title");

        tvBookingTitle = findViewById(R.id.tvBookingTitle);
        tvSelectedSeat = findViewById(R.id.tvSelectedSeat);
        spShowtimes = findViewById(R.id.spShowtimes);
        rvSeats = findViewById(R.id.rvSeats);
        btnConfirm = findViewById(R.id.btnConfirmBooking);

        tvBookingTitle.setText("Đặt vé phim: " + movieTitle);

        setupSeatGrid();
        loadShowtimes();

        spShowtimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < showtimeList.size()) {
                    checkBookedSeats(showtimeList.get(position).getShowtimeId());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnConfirm.setOnClickListener(v -> {
            if (spShowtimes.getSelectedItemPosition() < 0) {
                Toast.makeText(this, "Vui lòng chọn suất chiếu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSeatNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ghế ngồi", Toast.LENGTH_SHORT).show();
                return;
            }

            Showtime selectedShowtime = showtimeList.get(spShowtimes.getSelectedItemPosition());
            bookTicket(selectedShowtime);
        });
    }

    private void setupSeatGrid() {
        seatList.clear();
        String[] rows = {"A", "B", "C", "D", "E"};
        for (String row : rows) {
            for (int i = 1; i <= 6; i++) {
                seatList.add(new Seat(row + i, false));
            }
        }

        seatAdapter = new SeatAdapter(seatList, seat -> {
            for (Seat s : seatList) {
                if (s != seat) s.setSelected(false);
            }
            seat.setSelected(!seat.isSelected());
            selectedSeatNumber = seat.isSelected() ? seat.getSeatNumber() : "";
            tvSelectedSeat.setText("Ghế đã chọn: " + (selectedSeatNumber.isEmpty() ? "Chưa chọn" : selectedSeatNumber));
            seatAdapter.notifyDataSetChanged();
        });

        rvSeats.setLayoutManager(new GridLayoutManager(this, 6));
        rvSeats.setAdapter(seatAdapter);
    }

    private void loadShowtimes() {
        repository.getDb().collection("showtimes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    showtimeList.clear();
                    showtimeLabels.clear();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
                    
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Object fbMovieId = doc.get("movieId");
                        String fbMovieIdStr = fbMovieId != null ? fbMovieId.toString() : "";

                        if (fbMovieIdStr.equals(movieId)) {
                            Showtime st = new Showtime();
                            st.setShowtimeId(doc.getId());
                            st.setMovieId(fbMovieIdStr);
                            st.setTheaterId(doc.getString("theaterId"));
                            st.setStartTime(doc.getDate("startTime"));
                            
                            Object priceObj = doc.get("price");
                            double price = 0;
                            if (priceObj instanceof Long) price = ((Long) priceObj).doubleValue();
                            else if (priceObj instanceof Double) price = (Double) priceObj;
                            st.setPrice(price);

                            showtimeList.add(st);
                            showtimeLabels.add(sdf.format(st.getStartTime()) + " (" + (int)price + "đ)");
                        }
                    }
                    
                    if (showtimeList.isEmpty()) {
                        Toast.makeText(this, "Chưa có suất chiếu nào cho phim này", Toast.LENGTH_LONG).show();
                    } else {
                        updateSpinner();
                    }
                });
    }

    private void checkBookedSeats(String showtimeId) {
        for (Seat s : seatList) {
            s.setBooked(false);
            s.setSelected(false);
        }
        selectedSeatNumber = "";
        tvSelectedSeat.setText("Ghế đã chọn: Chưa chọn");

        repository.getDb().collection("tickets")
                .whereEqualTo("showtimeId", showtimeId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String bookedSeat = doc.getString("seatNumber");
                        for (Seat s : seatList) {
                            if (s.getSeatNumber().equals(bookedSeat)) {
                                s.setBooked(true);
                            }
                        }
                    }
                    seatAdapter.notifyDataSetChanged();
                });
    }

    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, showtimeLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShowtimes.setAdapter(adapter);
    }

    private void bookTicket(Showtime showtime) {
        String userId = repository.getCurrentUser().getUid();
        String ticketId = UUID.randomUUID().toString();
        // Lưu vé vào Firestore với thông tin chi tiết bao gồm tên phim
        Ticket ticket = new Ticket(ticketId, userId, showtime.getShowtimeId(), movieTitle, selectedSeatNumber, new Date(), showtime.getPrice(), "ĐÃ ĐẶT");

        repository.addTicket(ticket).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
            // Lập lịch thông báo nhắc giờ chiếu trước 30 phút
            scheduleNotification(showtime.getStartTime());
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi đặt vé: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void scheduleNotification(Date startTime) {
        if (startTime == null) return;
        
        Intent intent = new Intent(this, NotificationReceiver.class);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        intent.putExtra("scheduled_time", sdf.format(startTime));
        intent.putExtra("movie_title", movieTitle);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Tính toán thời gian thông báo: Giờ chiếu trừ đi 30 phút (30 * 60 * 1000 ms)
        long notificationTime = startTime.getTime() - (30 * 60 * 1000);
        
        // Nếu hiện tại đã quá thời gian nhắc (trước 30p), thì nhắc ngay sau 5 giây để test
        if (notificationTime < System.currentTimeMillis()) {
            notificationTime = System.currentTimeMillis() + 5000;
            Toast.makeText(this, "Suất chiếu sắp bắt đầu, thông báo sẽ gửi ngay!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sẽ nhắc bạn trước giờ chiếu 30 phút", Toast.LENGTH_SHORT).show();
        }

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        }
    }
}
