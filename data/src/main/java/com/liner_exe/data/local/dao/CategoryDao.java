package com.liner_exe.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.liner_exe.data.local.entities.CategoryEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CategoryDao {
    @Insert
    Completable insert(CategoryEntity category);

    @Query("SELECT * from categories")
    Flowable<List<CategoryEntity>> getAll();

    @Query("SELECT * from categories WHERE id = :categoryId")
    CategoryEntity findById(int categoryId);

    @Update
    Completable update(CategoryEntity category);

    @Query("DELETE from categories WHERE id = :id")
    Completable deleteById(int id);
}
