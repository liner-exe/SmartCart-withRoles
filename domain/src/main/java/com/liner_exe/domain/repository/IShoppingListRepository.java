package com.liner_exe.domain.repository;

import com.liner_exe.domain.models.ShoppingList;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IShoppingListRepository {
    Completable add(ShoppingList shoppingList);

    Flowable<List<ShoppingList>> getAll();

    Completable update(ShoppingList shoppingList);

    Completable deleteById(int id);
}