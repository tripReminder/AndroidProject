package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Trips")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int trip_id;
    @NonNull
    private String title;

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

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

    public Trip( String title, boolean status, String imagePath, String time, String date, String type, String from, String to, String repetition, String note) {
        this.trip_id = trip_id;
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

    public int getTrip_id() {
        return trip_id;
    }

    public String getTitle() {
        return title;
    }

    public boolean getStatus() {
        return status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getRepetition() {
        return repetition;
    }

    public String getNote() {
        return note;
    }
}
