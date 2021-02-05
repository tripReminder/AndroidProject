package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "Trips")
public class Trip implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int trip_id;
    @NonNull
    private String title;
    @NonNull
    private boolean status;
    @NonNull
    private String imagePath;
    @NonNull
    private String time;
    @NonNull
    private String date;
    @NonNull
    private String type;
    @NonNull
    private String from;
    @NonNull
    private String to;
    @NonNull
    private String repetition;
    @NonNull
    private String note;


    // constructor for add a trip to firebase
    public Trip() {

    }

    public Trip(String title, boolean status, String imagePath, String time, String date, String type, String from, String to, String repetition, String note) {
        this.title = title;
        this.status = status;
        this.imagePath = imagePath;
        this.time = time;
        this.date = date;
        this.type = type;
        this.from = from;
        this.to = to;
        this.repetition = repetition;
        this.note = note;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }


    // setter for firebase
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setImagePath(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setFrom(@NonNull String from) {
        this.from = from;
    }

    public void setTo(@NonNull String to) {
        this.to = to;
    }

    public void setRepetition(@NonNull String repetition) {
        this.repetition = repetition;
    }

    public void setNote(@NonNull String note) {
        this.note = note;
    }




    public int getTrip_id() {
        return trip_id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public boolean getStatus() {
        return status;
    }

    @NonNull
    public String getImagePath() {
        return imagePath;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getFrom() {
        return from;
    }

    @NonNull
    public String getTo() {
        return to;
    }

    @NonNull
    public String getRepetition() {
        return repetition;
    }

    @NonNull
    public String getNote() {
        return note;
    }
}
