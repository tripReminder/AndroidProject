package com.tripreminder.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_TRIP_ACTIVITY_REQUEST_CODE =1 ;
    private String TAG= this.getClass().getSimpleName();
    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , NewTrip.class);
                startActivityForResult(intent,NEW_TRIP_ACTIVITY_REQUEST_CODE);
            }
        });
        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            final String ID = UUID.randomUUID().toString();
            Trip  trip = new Trip(data.getStringExtra(NewTrip.TRIP_ADDED));
            tripViewModel.insert(trip);

            Toast.makeText(getApplicationContext(),"saved",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"not_saved",Toast.LENGTH_LONG).show();
        }
    }
}