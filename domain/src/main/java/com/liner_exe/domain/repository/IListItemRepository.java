package com.liner_exe.domain.repository;

import com.liner_exe.domain.models.ListItem;
import com.liner_exe.domain.models.monitoring.CategoryProgress;
import com.liner_exe.domain.models.monitoring.StoreProgress;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IListItemRepository {
    Completable add(ListItem listItem);

    Flowable<List<ListItem>> getItemsForList(int id, boolean sortByCategory);

    Completable update(ListItem listItem);

    Completable updateItemStatus(int itemId, int listId, int productId, boolean isBought);

    Completable deleteById(int itemId, int listId, int productId);

    Flowable<List<CategoryProgress>> getExpensesGroupedByCategory();

    Flowable<Double> getListTotalSum(int listId);

    Flowable<List<StoreProgress>> getExpensesGroupedByStore();
}
