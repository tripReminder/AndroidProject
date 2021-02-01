package com.tripreminder.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TripViewModel extends AndroidViewModel {

    //private String TAG = this.getClass().getSimpleName();
    private TripDao tripDao;
    private TripRoomDatabase tripRoomDatabase;

    public TripViewModel(@NonNull Application application) {
        super(application);

        tripRoomDatabase = TripRoomDatabase.getDatabase(application);
        tripDao = tripRoomDatabase.tripDao();
    }

    public void insert(Trip trip){
        new Thread(){
            @Override
            public void run() {
                super.run();
                tripDao.insert(trip);
            }
        }.start();
    }

    public Trip[] getAll(boolean status){
        Trip[] trips = tripDao.getAll(status);
        return trips;
    }

    public Trip getTrip(int id){
        Trip trip = tripDao.getTrip(id);
        return trip;
    }

    public void update(Trip trip){
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
