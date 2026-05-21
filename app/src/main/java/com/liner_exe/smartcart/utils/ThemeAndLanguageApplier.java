package com.liner_exe.smartcart.utils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public class ThemeAndLanguageApplier {
    public static void applyTheme(int savedThemeMode) {
        int appCompatMode;
        switch (savedThemeMode) {
            case 1:
                appCompatMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case 2:
                appCompatMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                appCompatMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }

        AppCompatDelegate.setDefaultNightMode(appCompatMode);
    }

    public static void applyLanguage(String languageCode) {
        if ("system".equals(languageCode)) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode));
        }
    }
}