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

    public Trip( String title, boolean status, String imagePath, String time, String date, String type, String from, String to, String repetition, String note) {
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
