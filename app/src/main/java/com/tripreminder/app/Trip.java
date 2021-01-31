package com.tripreminder.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "trip")
public class Trip {
    @PrimaryKey (autoGenerate = true)
    private int trip_id;

    private String  title;
    private boolean status;
    private String image_path;
    private String time;
    private String date;
    private String type;
    private String from;
    private String to;
    private String repetion;
    private String note;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setRepetion(String repetion) {
        this.repetion = repetion;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Trip(String title) {
        this.title = title;
        this.status = status;
        this.image_path = image_path;
        this.time = time;
        this.date = date;
        this.type = type;
        this.from = from;
        this.to = to;
        this.repetion = repetion;
        this.note = note;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }


    public int getTrip_id() {
        return trip_id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isStatus() {
        return status;
    }

    public String getImage_path() {
        return image_path;
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

    public String getRepetion() {
        return repetion;
    }

    public String getNote() {
        return note;
    }
}
