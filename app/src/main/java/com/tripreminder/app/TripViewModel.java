package com.tripreminder.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class TripViewModel extends AndroidViewModel {
    static Trip [] trips = new Trip[4];
    static boolean flag= false;
    private TripsDao tripDao;
    private TripsDatabase tripRoomDatabase;

    public TripViewModel(@NonNull Application application) {
        super(application);

        tripRoomDatabase = TripsDatabase.getDatabase(application);
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

    public void getAll(int status){
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


}
