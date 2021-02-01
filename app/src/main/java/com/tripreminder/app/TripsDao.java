package com.tripreminder.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TripsDao {

    @Insert
    void insert(Trip trips);
    @Query("SELECT * FROM Trips WHERE status = :status")
    Trip[] getAll(int status);

}
