package com.liner_exe.data.di;

import com.liner_exe.data.repository.CategoryRepositoryImpl;
import com.liner_exe.data.repository.ListItemRepositoryImpl;
import com.liner_exe.data.repository.ProductRepositoryImpl;
import com.liner_exe.data.repository.ShoppingListRepositoryImpl;
import com.liner_exe.data.repository.StoreRepositoryImpl;
import com.liner_exe.domain.repository.ICategoryRepository;
import com.liner_exe.domain.repository.IListItemRepository;
import com.liner_exe.domain.repository.IProductRepository;
import com.liner_exe.domain.repository.IShoppingListRepository;
import com.liner_exe.domain.repository.IStoreRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {
    @Binds
    @Singleton
    public abstract IProductRepository bindProductRepository(ProductRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract IShoppingListRepository bindShoppingListRepository(ShoppingListRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract ICategoryRepository bindCategoryRepository(CategoryRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract IListItemRepository bindListItemRepository(ListItemRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract IStoreRepository bindStoreRepository(StoreRepositoryImpl impl);
}
