package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpcomingTrip extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    FloatingActionButton addBtn, historyBtn;
    TripViewModel tripViewModel;
    Button closeAlert;
    static TextView noteLbl;
    static CardView noteView;
    public static final String TAG= "my tag";
    public static Trip[] data;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Trip");

    static IntentFilter s_intentFilter = new IntentFilter();
    AlarmReceiver alarmReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);
        addBtn = findViewById(R.id.addBtn);
        historyBtn = findViewById(R.id.historyBtn);
        noteLbl = findViewById(R.id.noteLbl);
        noteView = findViewById(R.id.noteView);
        closeAlert = findViewById(R.id.closeAlert);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){

        }else{
            if(!Settings.canDrawOverlays(UpcomingTrip.this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }else{

            }
        }

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
                startActivity(intent);
            }
        });

        closeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteView.setVisibility(View.INVISIBLE);
            }
        });

        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        registerReceiver(alarmReceiver, s_intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(alarmReceiver);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Trip Reminder");
        }
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setCheckedItem(R.id.nav_upcoming);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Trip[] trips = new Trip[4];
//        trips[0] = new Trip("Alex", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 1");
//        trips[1] = new Trip("Cairo", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 2");
//        trips[2] = new Trip("Aswan", true, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 3");
//        trips[3] = new Trip("Portsaid", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 4");
////
         tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
       //tripViewModel.insert(trips[3]);
         tripViewModel.getAll(false);

         while (!tripViewModel.flag){
             trips = tripViewModel.temp();
         }
         tripViewModel.flag=false;

        data = trips;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), tripViewModel, trips);
        recyclerView.setAdapter(adapter);

         //ReadRealData(tripViewModel);
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_upcoming:
                break;
            case R.id.nav_history:
                startActivity(new Intent(UpcomingTrip.this,TripsHistory.class));
                break;
            case R.id.nav_sync:
                ReadRealData(tripViewModel);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UpcomingTrip.this,RegisterUser.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}