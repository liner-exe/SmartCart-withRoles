package com.liner_exe.data.repository;

import com.liner_exe.data.local.dao.ListItemDao;
import com.liner_exe.data.mapper.ListItemMapper;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.domain.models.monitoring.CategoryProgress;
import com.liner_exe.domain.models.monitoring.StoreProgress;
import com.liner_exe.domain.repository.IListItemRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ListItemRepositoryImpl implements IListItemRepository {
    private final ListItemDao dao;

    @Inject
    public ListItemRepositoryImpl(ListItemDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable add(ListItem listItem) {
        return dao.insert(ListItemMapper.toEntity(listItem));
    }

    @Override
    public Flowable<List<ListItem>> getItemsForList(int id, boolean sortByCategory) {
        return dao.getItemsForList(id, sortByCategory).map(ListItemMapper::toModelList);
    }

    @Override
    public Completable update(ListItem listItem) {
        return dao.updateListItem(ListItemMapper.toEntity(listItem));
    }

    @Override
    public Completable updateItemStatus(int itemId, int listId, int productId, boolean isBought) {
        return dao.updateItemStatus(itemId, listId, productId, isBought);
    }

    @Override
    public Completable deleteById(int itemId, int listId, int productId) {
        return dao.deleteById(itemId, listId, productId);
    }

    @Override
    public Flowable<List<CategoryProgress>> getExpensesGroupedByCategory() {
        return dao.getExpensesGroupedByCategory().map(ListItemMapper::toCategoryProgressList);
    }

    @Override
    public Flowable<Double> getListTotalSum(int listId) {
        return dao.getListTotalSum(listId);
    }

    @Override
    public Flowable<List<StoreProgress>> getExpensesGroupedByStore() {
        return dao.getExpensesGroupedByStore().map(ListItemMapper::toStoreProgressList);
    }
}
