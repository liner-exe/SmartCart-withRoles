package com.liner_exe.data.di;

import android.content.Context;

import com.liner_exe.data.storage.SettingsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class StorageModule {
    @Provides
    @Singleton
    public SettingsManager provideSettingsManager(@ApplicationContext Context context) {
        return new SettingsManager(context);
    }
}
