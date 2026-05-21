package com.liner_exe.smartcart.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.liner_exe.data.storage.SettingsManager;
import com.liner_exe.domain.enums.Currency;
import com.liner_exe.smartcart.utils.ThemeAndLanguageApplier;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private final SettingsManager settingsManager;

    private final MutableLiveData<Integer> _themeMode = new MutableLiveData<>();
    public final LiveData<Integer> themeMode = _themeMode;

    private final MutableLiveData<String> _languageCode = new MutableLiveData<>();
    public final LiveData<String> languageCode = _languageCode;

    private final MutableLiveData<Currency> _currency = new MutableLiveData<>();
    public final LiveData<Currency> currency = _currency;

    @Inject
    public SettingsViewModel(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;

        _themeMode.setValue(settingsManager.getThemeMode());
        _languageCode.setValue(settingsManager.getLanguageCode());
        _currency.setValue(settingsManager.getCurrency());
    }

    public void changeThemeMode(int mode) {
        settingsManager.setThemeMode(mode);

        _themeMode.setValue(mode);

        ThemeAndLanguageApplier.applyTheme(mode);
    }

    public void changeLanguage(String code) {
        settingsManager.setLanguageCode(code);

        _languageCode.setValue(code);

        ThemeAndLanguageApplier.applyLanguage(code);
    }

    public void changeCurrency(Currency currency) {
        settingsManager.setCurrency(currency);
        _currency.setValue(currency);
    }
}