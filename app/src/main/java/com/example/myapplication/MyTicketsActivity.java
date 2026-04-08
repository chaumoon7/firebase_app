package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.models.Ticket;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MyTicketsActivity extends AppCompatActivity {
    private RecyclerView rvTickets;
    private TicketAdapter adapter;
    private List<Ticket> ticketList;
    private FirebaseRepository repository;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        repository = new FirebaseRepository();
        rvTickets = findViewById(R.id.rvTickets);
        toolbar = findViewById(R.id.toolbarTickets);

        toolbar.setNavigationOnClickListener(v -> finish());

        ticketList = new ArrayList<>();
        adapter = new TicketAdapter(ticketList);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        rvTickets.setAdapter(adapter);

        loadUserTickets();
    }

    private void loadUserTickets() {
        String userId = repository.getCurrentUser().getUid();
        repository.getUserTickets(userId).addOnSuccessListener(queryDocumentSnapshots -> {
            ticketList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Ticket ticket = doc.toObject(Ticket.class);
                if (ticket != null) {
                    ticketList.add(ticket);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }
}
