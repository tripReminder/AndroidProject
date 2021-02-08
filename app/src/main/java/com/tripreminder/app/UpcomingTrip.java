package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpcomingTrip extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addBtn, historyBtn;
    TripViewModel tripViewModel;
    public static final String TAG= "my tag";
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1;
    private Trip[] data;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Trip");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);
        addBtn = findViewById(R.id.addBtn);
        historyBtn = findViewById(R.id.historyBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpcomingTrip.this , NewTrip.class);
                intent.putExtra("type", "add");
                startActivity(intent);

            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpcomingTrip.this , TripsHistory.class);
                //intent.putExtra("type", "add");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Trip[] trips = new Trip[4];
//        trips[0] = new Trip("Alex", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");
//        trips[1] = new Trip("Cairo", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");
//        trips[2] = new Trip("Aswan", true, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");
//        trips[3] = new Trip("Portsaid", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "shgfakgkufefk");

         tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
       //tripViewModel.insert(trips[3]);
         tripViewModel.getAll(false);

         while (!tripViewModel.flag){
             trips = tripViewModel.temp();
         }
         tripViewModel.flag=false;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), tripViewModel, trips);
        recyclerView.setAdapter(adapter);

         ReadRealData(tripViewModel);
    }

    // read from firebase
    public void ReadRealData(TripViewModel tripViewModel)
    {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Trip[] trips = new Trip[(int)snapshot.getChildrenCount()];
                    int i = 0;
                    for (DataSnapshot datasnap : snapshot.getChildren()) {
                        Trip trip = datasnap.getValue(Trip.class);
                        trips[i] = trip;
                        i++;
                        Log.i(TAG,trip.getTitle());

                    }
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), tripViewModel, trips);
                    recyclerView.setAdapter(adapter);
                    Log.i(TAG,snapshot.getValue().toString());

                }
                else {
                    Log.i(TAG, " Read firebase");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



 /*   private void checkBubblesPermissions(Trip trip)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(UpcomingTrip.this)) {

                   Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                           Uri.parse("package:" + getPackageName()));
                   intent.putExtra("Intent",trip.getNote());
                  startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                    } else
                            startFloatingWidgetService(trip.getNote());
    }

    private void startFloatingWidgetService(String Notes) {
        Intent i = new Intent(this, bubbleService.class);
        i.setAction(bubbleService.ACTION_START);
        i.putExtra("Intent",Notes);
         startService(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                startFloatingWidgetService(getIntent().getStringExtra("Intent"));

        }
    }*/
}