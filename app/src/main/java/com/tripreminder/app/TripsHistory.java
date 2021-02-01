package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class TripsHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TripViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_history);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Trip[] trips = new Trip[4];
        trips[0] = new Trip(1, "Alex", 0, 0);
        trips[1] = new Trip(2, "Cairo", 1, 0);
        trips[2] = new Trip(3, "Aswan", 0, 0);
        trips[3] = new Trip(4, "Portsaid", 1, 0);

        // final String id = UUID.randomUUID().toString();
        viewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        viewModel.insert(trips[3]);
        viewModel.getAll(0);

        while (!viewModel.flag){
            trips = viewModel.temp();
        }

        recyclerView = findViewById(R.id.HistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), trips);
        recyclerView.setAdapter(adapter);
    }
}