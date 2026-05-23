package com.liner_exe.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.liner_exe.domain.enums.Currency;
import com.liner_exe.domain.enums.UserRole;

public class SettingsManager {
    private static final String PREF_NAME = "smartcart_roles_prefs";
    private static final String KEY_CURRENCY = "current_currency";
    private static final String KEY_THEME = "current_theme";
    private static final String KEY_LANGUAGE = "current_language";
    private static final String KEY_USER_ROLE = "user_role";

    private final SharedPreferences preferences;

    public SettingsManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setCurrency(Currency currency) {
        preferences.edit().putString(KEY_CURRENCY, currency.getCode()).apply();
    }

    public Currency getCurrency() {
        String code = preferences.getString(KEY_CURRENCY, Currency.DOLLAR.getCode());
        return Currency.fromCode(code);
    }

    public void setThemeMode(int themeMode) {
        preferences.edit().putInt(KEY_THEME, themeMode).apply();
    }

    public int getThemeMode() {
        return preferences.getInt(KEY_THEME, -1);
    }

    public void setLanguageCode(String languageCode) {
        preferences.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    public String getLanguageCode() {
        return preferences.getString(KEY_LANGUAGE, "system");
    }

    public void setUserRole(UserRole role) {
        if (role == null) return;

        preferences.edit().putString(KEY_USER_ROLE, role.name()).apply();
    }

    public UserRole getUserRole() {
        String roleName = preferences.getString(KEY_USER_ROLE, UserRole.REGULAR.name());

        try {
            return UserRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            return UserRole.REGULAR;
        }
    }

    public boolean isPremium() {
        return getUserRole() == UserRole.PREMIUM;
    }
}
