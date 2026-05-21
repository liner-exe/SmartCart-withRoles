package com.liner_exe.domain.repository;

import com.liner_exe.domain.models.Product;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IProductRepository {
    Completable add(Product product);

    Flowable<List<Product>> getAll();

    Product findById(int id);

    Completable update(Product product);

    Completable deleteById(int id);
}