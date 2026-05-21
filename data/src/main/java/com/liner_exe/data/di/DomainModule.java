package com.liner_exe.data.di;

import com.liner_exe.domain.usecases.GetGroupedShoppingListsUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DomainModule {
    @Provides
    @Singleton
    public GetGroupedShoppingListsUseCase provideGetGrouppedShoppingListsUseCase() {
        return new GetGroupedShoppingListsUseCase();
    }
}
