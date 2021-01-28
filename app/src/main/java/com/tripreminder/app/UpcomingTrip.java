package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class UpcomingTrip extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);

        Trip[] trips = new Trip[1];
        //test data
        trips[0] = new Trip("Alex", "10:00", "one way", "ismailia", "alex");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), trips);
        recyclerView.setAdapter(adapter);
    }
}