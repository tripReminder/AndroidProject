package com.tripreminder.app;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TripDao {

    @Insert
    void insert(Trip trip);

}
