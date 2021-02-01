package com.tripreminder.app;

import android.content.Context;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class}, version = 1)
public abstract class TripsDatabase extends RoomDatabase {
    public abstract TripsDao tripDao();
    private static volatile TripsDatabase tripRoomInstance;

    static TripsDatabase getDatabase(final Context context){
        if(tripRoomInstance == null){
            synchronized (TripsDatabase.class){
                if(tripRoomInstance == null){
                    tripRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            TripsDatabase.class,
                            "trip_database")
                            .build();

                }
            }
        }
        return tripRoomInstance;
    }
}
