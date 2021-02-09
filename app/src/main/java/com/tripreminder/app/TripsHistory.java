package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

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

        readRoom();
    }

    public void readRoom(){
        Trip[] trips = new Trip[4];
        // trips[0] = new Trip("Alex", 0, 0);
        //  trips[1] = new Trip("Cairo", 1, 0);
        //  trips[2] = new Trip(3, "Aswan", 0, 0);
        //  trips[3] = new Trip(4, "Portsaid", 1, 0);

        // final String id = UUID.randomUUID().toString();
        viewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        //viewModel.insert(trips[3]);
        viewModel.getAll(true);

        while (!viewModel.flag){
            trips = viewModel.temp();
        }
        viewModel.flag =false;

        recyclerView = findViewById(R.id.HistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), this, trips);
        recyclerView.setAdapter(adapter);
    }

    public void chickOverlayPermission(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 0);
    }

    public  void delete(Trip trip){
        viewModel.delete(trip);
        readRoom();
    }
}