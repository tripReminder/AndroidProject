package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class TripsHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_history);

        Trip[] trips = new Trip[1];
        trips[0] = new Trip("Alex", R.drawable.status,R.drawable.mytrip);
        trips[1] = new Trip("Cairo", R.drawable.status,R.drawable.mytrip);


        recyclerView = findViewById(R.id.HistoryList);
        layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(),trips);
        recyclerView.setAdapter(adapter);
    }
}