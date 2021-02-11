package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TripsHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TripViewModel viewModel;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    UpcomingTrip upcomingTrip =new UpcomingTrip();
    private static  final String MY_PREFS_NAME= "Shared prefrence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_history);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("Trip History");
        }
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener( this);
        navigationView.setCheckedItem(R.id.nav_history);

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
                startActivity(new Intent(TripsHistory.this,UpcomingTrip.class));
                break;
            case R.id.nav_history:
                break;
            case R.id.nav_sync:
                upcomingTrip.readFirebase();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.remove("userId");
                editor.apply();

                startActivity(new Intent(TripsHistory.this,RegisterUser.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}