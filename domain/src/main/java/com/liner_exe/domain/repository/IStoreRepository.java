package com.liner_exe.domain.repository;

import com.liner_exe.domain.models.Store;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IStoreRepository {
    Completable add(Store store);

    Flowable<List<Store>> getAll();

    Single<Store> findById(int id);

    Completable update(Store store);

    Completable deleteById(int id);
}
