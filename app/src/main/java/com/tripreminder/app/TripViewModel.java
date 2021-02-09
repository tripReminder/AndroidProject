package com.tripreminder.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class TripViewModel extends AndroidViewModel {
    static Trip [] trips = new Trip[4];
    static boolean flag= false;
    private TripDao tripDao;
    private TripRoomDatabase tripRoomDatabase;

    public TripViewModel(@NonNull Application application) {
        super(application);

        tripRoomDatabase = TripRoomDatabase.getDatabase(application);
        tripDao = tripRoomDatabase.tripDao();
    }

    public void insert(Trip...trip){
        new Thread(){
            @Override
            public void run() {
                super.run();
                tripDao.insert(trip);
            }
        }.start();
    }

    public void getAll(boolean status){
        new Thread(){
            @Override
            public void run() {
                super.run();
                 trips = tripDao.getAll(status);
                 flag = true;
            }
        }.start();
    }

    public Trip[] temp(){
        return trips;
    }

    public Trip getTrip(int id){
        Trip trip = tripDao.getTrip(id);
        return trip;
    }

    public void update(Trip...trip){
        new Thread(){
            @Override
            public void run() {
                super.run();
                tripDao.update(trip);
            }
        }.start();
    }

    public void delete(Trip trip){
        new Thread(){
            @Override
            public void run() {
                super.run();
                tripDao.delete(trip);
            }
        }.start();
    }
}
