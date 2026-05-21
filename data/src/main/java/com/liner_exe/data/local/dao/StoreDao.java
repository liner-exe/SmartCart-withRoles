package com.liner_exe.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.liner_exe.data.local.entities.StoreEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface StoreDao {
    @Insert
    Completable insert(StoreEntity shop);

    @Query("SELECT * FROM stores")
    Flowable<List<StoreEntity>> getAll();

    @Query("SELECT * FROM stores WHERE id = :storeId")
    StoreEntity findById(int storeId);

    @Update
    Completable update(StoreEntity shop);

    @Query("DELETE FROM stores WHERE id = :id")
    Completable deleteById(int id);
}
