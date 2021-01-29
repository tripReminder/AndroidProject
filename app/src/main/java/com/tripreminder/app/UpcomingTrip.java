package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

public class UpcomingTrip extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);
        addBtn = findViewById(R.id.addBtn);

        Trip[] trips = new Trip[1];
        //test data
        trips[0] = new Trip("Alex", "10:00", "one way", "ismailia", "alex");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), trips);
        recyclerView.setAdapter(adapter);
    }
}