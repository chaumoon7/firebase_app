package com.example.myapplication;

import com.example.myapplication.models.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseRepository {
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private final CollectionReference usersRef;
    private final CollectionReference moviesRef;
    private final CollectionReference theatersRef;
    private final CollectionReference showtimesRef;
    private final CollectionReference ticketsRef;

    public FirebaseRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        usersRef = db.collection("users");
        moviesRef = db.collection("movies");
        theatersRef = db.collection("theaters");
        showtimesRef = db.collection("showtimes");
        ticketsRef = db.collection("tickets");
    }

    // Auth methods
    public Task<AuthResult> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public void logout() {
        auth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Firestore methods
    public void addUser(User user) {
        usersRef.document(user.getUserId()).set(user);
    }

    public Task<DocumentSnapshot> getUser(String userId) {
        return usersRef.document(userId).get();
    }

    public void addMovie(Movie movie) {
        moviesRef.document(movie.getMovieId()).set(movie);
    }

    public Task<QuerySnapshot> getAllMovies() {
        return moviesRef.get();
    }

    public void addTheater(Theater theater) {
        theatersRef.document(theater.getTheaterId()).set(theater);
    }

    public void addShowtime(Showtime showtime) {
        showtimesRef.document(showtime.getShowtimeId()).set(showtime);
    }

    public Task<QuerySnapshot> getShowtimesForMovie(String movieId) {
        return showtimesRef.whereEqualTo("movieId", movieId).get();
    }

    public Task<Void> addTicket(Ticket ticket) {
        return ticketsRef.document(ticket.getTicketId()).set(ticket);
    }

    public Task<QuerySnapshot> getUserTickets(String userId) {
        return ticketsRef.whereEqualTo("userId", userId).get();
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
