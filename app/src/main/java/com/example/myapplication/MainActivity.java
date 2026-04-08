package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.models.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private FirebaseRepository repository;
    private ImageButton btnLogout;
    private FloatingActionButton fabTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new FirebaseRepository();
        if (repository.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        rvMovies = findViewById(R.id.rvMovies);
        btnLogout = findViewById(R.id.btnLogout);
        fabTickets = findViewById(R.id.fabTickets);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList, this::onMovieClick);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        loadMovies();

        btnLogout.setOnClickListener(v -> {
            repository.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        fabTickets.setOnClickListener(v -> {
            startActivity(new Intent(this, MyTicketsActivity.class));
        });
    }

    private void loadMovies() {
        repository.getAllMovies().addOnSuccessListener(queryDocumentSnapshots -> {
            movieList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                try {
                    String id = doc.getId();
                    String title = doc.getString("title");
                    String desc = doc.getString("description");
                    
                    // Lấy link ảnh - Kiểm tra cả 2 trường hợp đặt tên
                    String poster = doc.getString("posterUrl");
                    if (poster == null) poster = doc.getString("posterURL");
                    
                    String genre = doc.getString("genre");
                    
                    long duration = 0;
                    Object durObj = doc.get("duration");
                    if (durObj instanceof Long) duration = (Long) durObj;
                    else if (durObj instanceof String) duration = Long.parseLong((String) durObj);

                    double rating = 0;
                    Object ratObj = doc.get("rating");
                    if (ratObj instanceof Double) rating = (Double) ratObj;
                    else if (ratObj instanceof Long) rating = ((Long) ratObj).doubleValue();
                    else if (ratObj instanceof String) rating = Double.parseDouble((String) ratObj);

                    // Đảm bảo thứ tự tham số đúng: id, title, description, posterUrl, duration, genre, rating
                    Movie movie = new Movie(id, title, desc, poster, duration, genre, rating);
                    movieList.add(movie);
                    
                    Log.d("MainActivity", "Loaded movie: " + title + " | Image: " + poster);
                } catch (Exception e) {
                    Log.e("MainActivity", "Lỗi xử lý phim: " + doc.getId(), e);
                }
            }
            adapter.notifyDataSetChanged();
            
            if (movieList.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy phim trên Firebase!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("movie_id", movie.getMovieId());
        intent.putExtra("movie_title", movie.getTitle());
        startActivity(intent);
    }
}
