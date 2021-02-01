package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Trips")
public class Trip {

    @PrimaryKey
    @NonNull
    private int trip_id;
    @NonNull
    private String title;
    @NonNull
    private int status;
    @NonNull
    private int imagePath;


    public Trip(int trip_id, @NonNull String title, int status, @NonNull int imagePath) {
        this.trip_id = trip_id;
        this.title = title;
        this.status = status;
        this.imagePath = imagePath;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setImagePath(@NonNull int imagePath) {
        this.imagePath = imagePath;
    }


    public int getTrip_id() {
        return trip_id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    @NonNull
    public int getImagePath() {
        return imagePath;
    }
}
