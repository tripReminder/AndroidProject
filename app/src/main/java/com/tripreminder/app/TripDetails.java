package com.tripreminder.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class TripDetails extends AppCompatActivity {

    TextView title ,from, to ,time , date ,type ;
    TextView repetition ,re_time , re_date;
    TextView textView1 ,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details);

        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int hight = dm.heightPixels;

        getWindow().setLayout((int)(width*.92),(int)(hight*.75));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x =0;
        params.y= -20;
        getWindow().setAttributes(params);

        title = findViewById(R.id.txt_title);
        time = findViewById(R.id.txt_time);
        date = findViewById(R.id.txt_date);
        type = findViewById(R.id.txt_type);
        from = findViewById(R.id.txt_from);
        to = findViewById(R.id.txt_to);
        re_time = findViewById(R.id.txt_Rtime);
        re_date = findViewById(R.id.txt_Rdate);
        repetition =findViewById(R.id.txt_repetition);
        textView1=findViewById(R.id.view_date);
        textView2=findViewById(R.id.view_time);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        title.setText(trip.getTitle());
        time.setText(trip.getTime());
        date.setText(trip.getDate());
        from.setText(trip.getFrom());
        to.setText(trip.getTo());
        repetition.setText(trip.getRepetition());
        type.setText(trip.getType());
        if(trip.getType().equals("Round Trip")) {
            re_date.setVisibility(View.VISIBLE);
            re_time.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            re_date.setText(trip.getRoundDate());
            re_time.setText(trip.getRoundTime());
        }

    }
}