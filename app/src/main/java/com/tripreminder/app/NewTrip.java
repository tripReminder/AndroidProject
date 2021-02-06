package com.tripreminder.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

public class NewTrip extends AppCompatActivity  {
    Spinner spinner , spinner2;
    TimePickerDialog pickerTime;
    DatePickerDialog pickerDate;
    String trip_type, trip_repeation;
    TextView txtTime , txtDate;
    EditText tripName;
    EditText startPoint;
    EditText endPoint;
    EditText note;
    TripViewModel tripViewModel;
    Trip trip;
    Double start_lat, start_lng;
    Double end_lat, end_lng;
    ImageView roundTime ,roundDate;
    TextView txtRoundTime , txtRoundDate;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Trip");


    public static final String TRIP_ADDED= "new_trip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        // Save data in firebase by Asmaa
        trip = new Trip();


        spinner = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        txtTime = findViewById(R.id.txtTime);
        txtDate = findViewById(R.id.txtDate);
        ImageView imageTime = findViewById(R.id.imageTime);
        ImageView imageDate = findViewById(R.id.imageDate);
        tripName = findViewById(R.id.edtTripName);
        startPoint = findViewById(R.id.edtStartPoint);
        endPoint = findViewById(R.id.edtEndPoint);
        note = findViewById(R.id.edtNotes);
        Button btnAddTrip = findViewById(R.id.btnAdd);
        Button btnCancel =findViewById(R.id.btnCancel);
        roundTime = findViewById(R.id.roundTime);
        roundDate = findViewById(R.id.roundDate);
        txtRoundTime= findViewById(R.id.txtRoundTime);
        txtRoundDate= findViewById(R.id.txtRoundDate);

        spinnerTripType();
        spinnerTripRepetion();

        if(getIntent().getStringExtra("type").equals("add")){
            btnAddTrip.setText(R.string.add);
        }else if((getIntent().getStringExtra("type").equals("update"))){
            btnAddTrip.setText(R.string.update);

            Trip trip = (Trip) getIntent().getSerializableExtra("trip");

            tripName.setText(trip.getTitle());
            startPoint.setText(trip.getFrom());
            endPoint.setText(trip.getTo());
            note.setText(trip.getNote());
            txtTime.setText(trip.getTime());
            txtDate.setText(trip.getDate());

            if(trip.getType().equals("One Way Trip")){
                spinner.setSelection(0);
            }else if(trip.getType().equals("Round Trip")){
                spinner.setSelection(1);
            }

            if(trip.getRepetition().equals("No Repeat")){
                spinner2.setSelection(0);
            }else if(trip.getType().equals("Repeat Daily")){
                spinner2.setSelection(1);
            }else if(trip.getType().equals("Repeat Weekly")){
                spinner2.setSelection(2);
            }else if(trip.getType().equals("Repeat Monthly")){
                spinner2.setSelection(3);
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("Round Trip")){
                    roundTime.setVisibility(View.VISIBLE);
                    roundDate.setVisibility(View.VISIBLE);
                    txtRoundTime.setVisibility(View.VISIBLE);
                    txtRoundDate.setVisibility(View.VISIBLE);
                }else{
                    roundTime.setVisibility(View.INVISIBLE);
                    roundDate.setVisibility(View.INVISIBLE);
                    txtRoundTime.setVisibility(View.INVISIBLE);
                    txtRoundDate.setVisibility(View.INVISIBLE);
                    txtRoundTime.setText("");
                    txtRoundDate.setText("");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        roundTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRoundTime();
            }
        });

        roundDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRoundDate();
            }
        });

        imageTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });

        imageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip_type = spinner.getSelectedItem().toString().trim();
                trip_repeation=spinner2.getSelectedItem().toString().trim();

                String t_title = tripName.getText().toString();
                String t_time = txtTime.getText().toString();
                String t_Date = txtDate.getText().toString();
                String t_type = trip_type;
                String t_from = startPoint.getText().toString();
                String t_to = endPoint.getText().toString();
                String t_repeation = trip_repeation;
                String t_note = note.getText().toString();
                String t_roundTime= txtRoundTime.getText().toString();
                String t_roundDate=txtRoundDate.getText().toString();

                Trip model = new Trip(t_title,false,"",t_time,t_Date,t_type,t_from,t_to,
                        t_repeation,t_note,start_lat,start_lng,end_lat,end_lng ,t_roundTime,t_roundDate);
                tripViewModel = ViewModelProviders.of(NewTrip.this).get(TripViewModel.class);

                if(getIntent().getStringExtra("type").equals("add")){
                    tripViewModel.insert(model);
                }else if(getIntent().getStringExtra("type").equals("update")){
                    Trip trip = (Trip) getIntent().getSerializableExtra("trip");
                    model.setTrip_id(trip.getTrip_id());
                    tripViewModel.update(model);
                }

                Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                // firebase
                SaveRealData();
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewTrip.this , MainActivity.class));
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyBNzGxWu5UWgITiyHulPmhJBDcniPf-bEY");

        startPoint.setFocusable(false);
        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(NewTrip.this);
                startActivityForResult(intent, 100);
            }
        });

        endPoint.setFocusable(false);
        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(NewTrip.this);
                startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            startPoint.setText(place.getAddress());
            start_lat=place.getLatLng().latitude;
            start_lng=place.getLatLng().longitude;
        }else if(requestCode == 200 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            endPoint.setText(place.getAddress());
            end_lat=place.getLatLng().latitude;
            end_lng=place.getLatLng().longitude;
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void getTime() {
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        pickerTime = new TimePickerDialog(NewTrip.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        txtTime.setText(sHour + ":" + sMinute);

                    }
                }, hour, minutes, true);
        pickerTime.show();
    }
    private void getDate() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        pickerDate = new DatePickerDialog(NewTrip.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        pickerDate.show();
    }


    private void spinnerTripType() {
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.trip_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void spinnerTripRepetion() {
        ArrayAdapter<CharSequence> adapter2 =ArrayAdapter.createFromResource(this,R.array.trip_repetition,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }

 // write data to firebase
    public void SaveRealData()
    {
        String tripTitle = tripName.getText().toString().trim();
        String tripTime = txtTime.getText().toString().trim();
        String tripDate = txtDate.getText().toString().trim();
        String tripType = trip_type;
        String tripFrom = startPoint.getText().toString().trim();
        String tripTo = endPoint.getText().toString().trim();
        String tripRepeation = trip_repeation;
        String tripNote = note.getText().toString().trim();

        trip.setTitle(tripTitle);
        trip.setTime(tripTime);
        trip.setDate(tripDate);
        trip.setType(tripType);
        trip.setFrom(tripFrom);
        trip.setTo(tripTo);
        trip.setRepetition(tripRepeation);
        trip.setNote(tripNote);

        myRef.push().setValue(trip);
        Toast.makeText(NewTrip.this, "New trip is added", Toast.LENGTH_LONG).show();


    }
    private void getRoundTime() {
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        pickerTime = new TimePickerDialog(NewTrip.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        txtRoundTime.setText(sHour + ":" + sMinute);

                    }
                }, hour, minutes, true);
        pickerTime.show();
    }
    private void getRoundDate() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        pickerDate = new DatePickerDialog(NewTrip.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtRoundDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        pickerDate.show();
    }
}