package com.liner_exe.data.di;


import android.content.Context;

import androidx.room.Room;

import com.liner_exe.data.local.AppDatabase;
import com.liner_exe.data.local.dao.CategoryDao;
import com.liner_exe.data.local.dao.ListItemDao;
import com.liner_exe.data.local.dao.ProductDao;
import com.liner_exe.data.local.dao.ShoppingListDao;
import com.liner_exe.data.local.dao.StoreDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                AppDatabase.DATABASE_NAME
        ).build();
    }

    @Provides
    public ProductDao provideProductDao(AppDatabase db) {
        return db.productDao();
    }

    @Provides
    public ShoppingListDao provideShoppingListDao(AppDatabase db) {
        return db.shoppingListDao();
    }

    @Provides
    public ListItemDao provideListItemDao(AppDatabase db) {
        return db.listItemDao();
    }

    @Provides
    public CategoryDao provideCategoryDao(AppDatabase db) {
        return db.categoryDao();
    }

    @Provides
    public StoreDao provideStoreDao(AppDatabase db) {
        return db.storeDao();
    }
}
