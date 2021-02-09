package com.tripreminder.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TripDao {

    @Insert
    void insert(Trip...trip);

    @Query("SELECT * FROM Trips WHERE status = :status")
    Trip[] getAll(boolean status);

    @Query("SELECT * FROM Trips WHERE trip_id = :id")
    Trip getTrip(int id);

    @Update
    void update(Trip...trip);

    @Delete
    int delete(Trip trip);
}
