package com.liner_exe.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.liner_exe.data.local.dto.ShoppingListWithProgressDto;
import com.liner_exe.data.local.entities.ListItemEntity;
import com.liner_exe.data.local.entities.ShoppingListEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ShoppingListDao {
    @Insert
    Completable insert(ShoppingListEntity shoppingList);

//    @Query("SELECT * from shopping_lists")
//    Flowable<List<ShoppingListEntity>> getAllLists();

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    Flowable<List<ShoppingListEntity>> getAllSorted();

    @Update
    Completable update(ShoppingListEntity shoppingList);

    @Query("DELETE from shopping_lists WHERE id = :id")
    Completable deleteById(int id);

    // ListItem

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertListItem(ListItemEntity listItem);

    @Query("DELETE FROM list_items WHERE listId = :listId AND productId = :productId")
    Completable deleteProductFromList(int listId, int productId);

    @Transaction
    @Query("SELECT " +
            "sl.id, " +
            "sl.name, " +
            "sl.createdAt, " +
            "COUNT(li.productId) AS totalItems, " +
            "SUM(CASE WHEN li.isChecked = 1 THEN 1 ELSE 0 END) AS boughtItems " +
            "FROM shopping_lists sl " +
            "LEFT JOIN list_items li ON sl.id = li.listId " +
            "GROUP BY sl.id, sl.name, sl.createdAt " +
            "ORDER BY sl.createdAt, sl.id ")
    Flowable<List<ShoppingListWithProgressDto>> getAllLists();
}
