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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class UpcomingTrip extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    FloatingActionButton addBtn, historyBtn;
    TripViewModel tripViewModel;
    Button closeAlert;
    static TextView noteLbl;
    static CardView noteView;
    public static final String TAG= "my tag";
    public static Trip[] data = new Trip[0];
    private static  final String MY_PREFS_NAME= "Shared prefrence";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Trip");

    static IntentFilter s_intentFilter = new IntentFilter();
    AlarmReceiver alarmReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);
        addBtn = findViewById(R.id.addBtn);
        noteLbl = findViewById(R.id.noteLbl);
        noteView = findViewById(R.id.noteView);
        closeAlert = findViewById(R.id.closeAlert);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpcomingTrip.this , NewTrip.class);
                intent.putExtra("type", "add");
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



        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Upcoming Trip");
        }
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_upcoming);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(alarmReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readRoom();
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
                readFirebase();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.remove("userId");
                editor.apply();

                startActivity(new Intent(UpcomingTrip.this,RegisterUser.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void readRoom(){
        Trip[] trips = new Trip[4];
//        trips[0] = new Trip("Alex", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 1");
//        trips[1] = new Trip("Cairo", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 2");
//        trips[2] = new Trip("Aswan", true, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 3");
//        trips[3] = new Trip("Portsaid", false, "g", "jj", "ff", "on way", "ismailia", "alex", "no", "note 4");
////
        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        tripViewModel.getAll(false);

        while (!TripViewModel.flag){
            data = tripViewModel.temp();
        }
        TripViewModel.flag=false;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), this, data);
        recyclerView.setAdapter(adapter);

        SharedPreferences.Editor editor = getSharedPreferences("receiver", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("data",json);
        editor.apply();
    }

    // read from firebase
    public void readFirebase() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Trip[] trips = new Trip[(int)snapshot.getChildrenCount()];
                    int i = 0;
                    for (DataSnapshot datasnap : snapshot.getChildren()) {
                        Trip trip = datasnap.getValue(Trip.class);
                        trips[i] = trip;
                        i++;
                    }

                    sync(trips, data);
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

    private void sync(Trip[] firebaseTrips, Trip[] roomTrips){
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                readRoom();
                Log.i("tag","handler sync");
                return false;
            }
        });

        new Thread(){
            @Override
            public void run() {
                super.run();
                Log.i("tag","thread");

                boolean flag = false;
                for (Trip firebaseTrip : firebaseTrips) {

                    for (Trip roomTrip : roomTrips) {
                        if(roomTrip.getTrip_id() == firebaseTrip.getTrip_id()) {
                            tripViewModel.update(firebaseTrip);
                            flag = true;
                        }
                    }

                    if(!flag) {
                        tripViewModel.insert(firebaseTrip);
                    }

                    flag = false;
                }


                for (Trip roomTrip : roomTrips) {
                     myRef.child(roomTrip.getTrip_id()+"").setValue(roomTrip);
                }
                Log.i("tag",firebaseTrips.length +"");
                Log.i("tag",roomTrips.length + "");


                Message message = new Message();
                handler.sendMessage(message);
            }
        }.start();
    }

    public void chickOverlayPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
    }

    public  void delete(Trip trip){
        tripViewModel.delete(trip);
        readRoom();
    }
    public  void updateStatus(Trip trip){
        tripViewModel.update(trip);
        readRoom();
    }
}