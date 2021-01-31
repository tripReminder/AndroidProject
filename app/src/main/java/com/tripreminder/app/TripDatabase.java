package com.tripreminder.app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class}, version = 1)
public abstract class TripDatabase extends RoomDatabase {

    public abstract TripDao tripDao();

    private static volatile TripDatabase tripRoomInstance;

    static TripDatabase getDatabase(final Context context){
        if(tripRoomInstance == null){
            synchronized (TripDatabase.class){
                if(tripRoomInstance == null){
                    tripRoomInstance= Room.databaseBuilder(context.getApplicationContext(),
                            TripDatabase.class,"trip_database")
                            .build();
                }
            }
        }
        return tripRoomInstance;
    }
}
