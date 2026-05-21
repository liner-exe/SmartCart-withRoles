package com.liner_exe.data.repository;

import com.liner_exe.data.local.dao.ShoppingListDao;
import com.liner_exe.data.mapper.ShoppingListMapper;
import com.liner_exe.domain.models.ShoppingList;
import com.liner_exe.domain.repository.IShoppingListRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ShoppingListRepositoryImpl implements IShoppingListRepository {
    private final ShoppingListDao dao;

    @Inject
    public ShoppingListRepositoryImpl(ShoppingListDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable add(ShoppingList shoppingList) {
        return dao.insert(ShoppingListMapper.toEntity(shoppingList));
    }

    @Override
    public Flowable<List<ShoppingList>> getAll() {
        return dao.getAllLists().map(ShoppingListMapper::toModelList);
    }

    @Override
    public Completable update(ShoppingList shoppingList) {
        return dao.update(ShoppingListMapper.toEntity(shoppingList));
    }

    @Override
    public Completable deleteById(int id) {
        return dao.deleteById(id);
    }
}