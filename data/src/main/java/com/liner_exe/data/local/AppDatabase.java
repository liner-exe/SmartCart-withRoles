package com.liner_exe.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.liner_exe.data.local.dao.CategoryDao;
import com.liner_exe.data.local.dao.ListItemDao;
import com.liner_exe.data.local.dao.ProductDao;
import com.liner_exe.data.local.dao.ShoppingListDao;
import com.liner_exe.data.local.dao.StoreDao;
import com.liner_exe.data.local.entities.CategoryEntity;
import com.liner_exe.data.local.entities.ListItemEntity;
import com.liner_exe.data.local.entities.ProductEntity;
import com.liner_exe.data.local.entities.ShoppingListEntity;
import com.liner_exe.data.local.entities.StoreEntity;

@Database(entities = {
        ProductEntity.class,
        ShoppingListEntity.class,
        ListItemEntity.class,
        CategoryEntity.class,
        StoreEntity.class
        },
        version = 4
)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "smartcart_db";

    public abstract ProductDao productDao();
    public abstract ShoppingListDao shoppingListDao();
    public abstract ListItemDao listItemDao();
    public abstract CategoryDao categoryDao();
    public abstract StoreDao storeDao();
}