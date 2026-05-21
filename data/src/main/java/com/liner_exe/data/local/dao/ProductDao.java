package com.liner_exe.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.liner_exe.data.local.entities.ProductEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ProductDao {
    @Insert
    Completable insert(ProductEntity product);

    @Query("SELECT * from products")
    Flowable<List<ProductEntity>> getAll();

    @Query("SELECT * from products WHERE id = :id")
    ProductEntity findById(int id);

    @Query("SELECT * from products WHERE name = :name")
    List<ProductEntity> findByName(String name);

    @Update
    Completable update(ProductEntity product);

    @Query("DELETE from products WHERE id = :id")
    Completable deleteById(int id);
}
