package com.tripreminder.app;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.loader.content.AsyncTaskLoader;

public class TripViewModel extends AndroidViewModel {

    private String TAG =this.getClass().getSimpleName();
    private TripDao tripDao;
    private TripDatabase tripDB;

    public TripViewModel( Application application) {
        super(application);
        tripDB = TripDatabase.getDatabase(application);
        tripDao = tripDB.tripDao();
    }
    public void insert(Trip trip){
        new InsertAsyncTask(tripDao).execute(trip);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"ViewModel Destroyed");
    }
    private class InsertAsyncTask extends AsyncTask<Trip ,Void,Void> {
        TripDao mtripDao;

        public InsertAsyncTask(TripDao mtripDao) {
            this.mtripDao= mtripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            mtripDao.insert(trips[0]);
            return null;
        }
    }
}
