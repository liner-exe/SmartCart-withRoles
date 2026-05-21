package com.liner_exe.domain.repository;

import com.liner_exe.domain.models.Category;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface ICategoryRepository {
    Completable add(Category category);

    Flowable<List<Category>> getAll();

    Single<Category> findById(int categoryId);

    Completable update(Category category);

    Completable deleteById(int id);
}
