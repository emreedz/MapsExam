package com.emre.javamapsexamp.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.emre.javamapsexamp.model.place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place")
    Flowable<List<place>> getAll();


    @Insert
    Completable insert(place places);

    @Delete
    Completable delete(place places);

}
