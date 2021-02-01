package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

public class UpcomingTrip extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addBtn;
    TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);
        addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

       Trip[] trips = new Trip[4];
        trips[0] = new Trip(1, "Alex", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");
        trips[1] = new Trip(2, "Cairo", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");
        trips[2] = new Trip(3, "Aswan", true, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");
        trips[3] = new Trip(4, "Portsaid", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");

       // final String id = UUID.randomUUID().toString();
         tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
       tripViewModel.insert(trips[3]);
         tripViewModel.getAll(false);

         while (!tripViewModel.flag){
             trips = tripViewModel.temp();
         }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), trips);
        recyclerView.setAdapter(adapter);
    }
}