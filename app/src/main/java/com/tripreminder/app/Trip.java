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
    @NonNull
    private Double start_lat;
    @NonNull
    private Double start_lng;
    @NonNull
    private Double end_lat;
    @NonNull
    private Double end_lng;


    // constructor for add a trip to firebase
    public Trip() {

    }

    public Trip(String title, boolean status, String imagePath, String time, String date, String type, String from, String to,
                String repetition, String note,  Double start_lat, Double start_lng, Double end_lat, Double end_lng) {
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
        this.start_lat = start_lat;
        this.start_lng = start_lng;
        this.end_lat = end_lat;
        this.end_lng = end_lng;
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

    public Double getStart_lat() {
        return start_lat;
    }

    @NonNull
    public Double getStart_lng() {
        return start_lng;
    }

    @NonNull
    public Double getEnd_lat() {
        return end_lat;
    }

    @NonNull
    public Double getEnd_lng() {
        return end_lng;
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

    public void setStart_lat(@NonNull Double start_lat) {
        this.start_lat = start_lat;
    }

    public void setStart_lng(@NonNull Double start_lng) {
        this.start_lng = start_lng;
    }

    public void setEnd_lat(@NonNull Double end_lat) {
        this.end_lat = end_lat;
    }

    public void setEnd_lng(@NonNull Double end_lng) {
        this.end_lng = end_lng;
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
