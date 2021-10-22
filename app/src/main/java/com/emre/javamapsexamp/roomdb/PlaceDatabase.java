package com.emre.javamapsexamp.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.emre.javamapsexamp.model.place;

@Database(entities = {place.class}, version = 1)
public abstract class PlaceDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();

}
