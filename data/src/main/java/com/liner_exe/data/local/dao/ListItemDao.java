package com.liner_exe.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.liner_exe.data.local.dto.ListItemDto;
import com.liner_exe.data.local.dto.monitoring.CategoryProgressDto;
import com.liner_exe.data.local.dto.monitoring.StoreProgressDto;
import com.liner_exe.data.local.entities.ListItemEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ListItemEntity listItem);

    @Query("SELECT " +
            "li.id AS id, " +
            "p.id AS productId, " +
            "li.listId AS listId, " +
            "p.categoryId AS categoryId, " +
            "li.storeId AS storeId, " +
            "p.name AS productName, " +
            "li.quantity AS quantity, " +
            "li.price AS price, " +
            "li.unit AS unit, " +
            "li.isChecked AS isChecked, " +
            "c.name AS categoryName, " +
            "c.emoji AS categoryEmoji " +
            "FROM list_items li " +
            "LEFT JOIN products p ON li.productId = p.id " +
            "LEFT JOIN categories c ON p.categoryId = c.id " +
            "WHERE li.listId = :listId " +
            "ORDER BY " +
            "  li.isChecked ASC," +
            "  CASE WHEN :sortByCategory THEN c.name END ASC, " +
            "  li.id DESC "
    )
    Flowable<List<ListItemDto>> getItemsForList(int listId, boolean sortByCategory);

    @Update
    Completable updateListItem(ListItemEntity listItem);

    @Query("UPDATE list_items SET isChecked = :isBought " +
            "WHERE productId = :productId " +
            "AND listId = :listId " +
            "AND id = :itemId")
    Completable updateItemStatus(int itemId, int listId, int productId, boolean isBought);

    @Query("DELETE FROM list_items " +
            "WHERE productId = :productId " +
            "AND listId = :listId " +
            "AND id = :itemId")
    Completable deleteById(int itemId, int listId, int productId);

    @Query("SELECT " +
            "  COALESCE(c.name, 'No category') AS categoryName, " +
            "  COALESCE(c.emoji, '📦') AS categoryEmoji, " +
            "  SUM(li.quantity * li.price) AS totalExpense " +
            "FROM list_items li " +
            "LEFT JOIN products p ON li.productId = p.id " +
            "LEFT JOIN categories c ON p.categoryId = c.id " +
            "WHERE li.isChecked = 1 " +
            "GROUP BY p.categoryId, c.name, c.emoji"
    )
    Flowable<List<CategoryProgressDto>> getExpensesGroupedByCategory();

    @Query("SELECT COALESCE(SUM(quantity * price), 0.0) FROM list_items WHERE listId = :listId AND isChecked = 1")
    Flowable<Double> getListTotalSum(int listId);

    @Query("SELECT " +
            "  COALESCE(s.name, 'No store') AS storeName, " +
            "  SUM(li.quantity * li.price) AS totalExpense " +
            "FROM list_items li " +
            "LEFT JOIN stores s ON li.storeId = s.id " +
            "WHERE li.isChecked = 1 " +
            "GROUP BY li.storeId, s.name ")
    Flowable<List<StoreProgressDto>> getExpensesGroupedByStore();
}