package com.tripreminder.app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Trip.class, version = 3)
public abstract class TripRoomDatabase  extends RoomDatabase {

    public abstract TripDao tripDao();

    private static volatile TripRoomDatabase tripRoomInstance;

    static TripRoomDatabase getDatabase(final Context context){
        if(tripRoomInstance == null){
            synchronized (TripRoomDatabase.class){
                if(tripRoomInstance == null){
                    tripRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            TripRoomDatabase.class, "trip_database")
                            .build();
                }
            }
        }
        return tripRoomInstance;
    }
}
