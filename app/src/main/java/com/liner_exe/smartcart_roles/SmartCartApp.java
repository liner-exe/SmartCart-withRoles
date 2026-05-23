package com.liner_exe.smartcart_roles;

import android.app.Application;

import com.liner_exe.data.storage.SettingsManager;
import com.liner_exe.smartcart_roles.utils.ThemeAndLanguageApplier;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SmartCartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SettingsManager settingsManager = new SettingsManager(this);

        ThemeAndLanguageApplier.applyTheme(settingsManager.getThemeMode());
        ThemeAndLanguageApplier.applyLanguage(settingsManager.getLanguageCode());
    }
}
