package com.liner_exe.smartcart_roles.fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.liner_exe.domain.enums.Currency;
import com.liner_exe.domain.enums.UserRole;
import com.liner_exe.smartcart_roles.R;
import com.liner_exe.smartcart_roles.databinding.FragmentSettingsBinding;
import com.liner_exe.smartcart_roles.utils.PremiumDialogManager;
import com.liner_exe.smartcart_roles.viewmodel.SettingsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentSettings extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;

    private int premiumTapCount = 0;
    private final Handler clickHandler = new Handler(Looper.getMainLooper());
    private Runnable clickRunnable = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        setupObservers();

        setupClickListeners();
    }

    private void setupObservers() {
        settingsViewModel.currency.observe(getViewLifecycleOwner(), currency -> {
            binding.currencyChangeButton.setSubtitle(currency.getCode());
        });

        settingsViewModel.themeMode.observe(getViewLifecycleOwner(), mode -> {
            String themeDescription;

            switch (mode) {
                case 1:
                    themeDescription = getString(R.string.theme_light);
                    break;
                case 2:
                    themeDescription = getString(R.string.theme_dark);
                    break;
                default:
                    themeDescription = getString(R.string.settings_system_defaults);
                    break;
            }

            binding.themeChangeButton.setSubtitle(themeDescription);
        });

        settingsViewModel.languageCode.observe(getViewLifecycleOwner(), code -> {
            String langDescription = getString(R.string.settings_system_defaults);
            if ("ru".equals(code)) langDescription = getString(R.string.lang_ru);
            if ("en".equals(code)) langDescription = getString(R.string.lang_en);

            binding.languageChangeButton.setSubtitle(langDescription);
        });
    }

    private void setupClickListeners() {
        NavController navController = Navigation
                .findNavController(requireActivity(), R.id.main_nav_host);

        binding.productsManagementButton.setOnClickListener(v -> {
            NavDirections action = MainFragmentDirections
                    .actionMainFragmentToProductsManagementFragment();
            navController.navigate(action);
        });

        binding.categoriesManagementButton.setOnClickListener(v -> {
            NavDirections action = MainFragmentDirections
                    .actionMainFragmentToCategoryManagementFragment();
            navController.navigate(action);
        });

        binding.storesManagementButton.setOnClickListener(v -> {
            if (settingsViewModel.userRole.getValue() == UserRole.PREMIUM) {
                NavDirections action = MainFragmentDirections
                        .actionMainFragmentToStoresManagementFragment();
                navController.navigate(action);
            } else {
                PremiumDialogManager.showPremiumInfoDialog(requireContext(), settingsViewModel);
            }
        });

        binding.currencyChangeButton.setOnClickListener(v -> {
            if (settingsViewModel.userRole.getValue() == UserRole.PREMIUM) {
                showCurrencySelectionDialog();
            } else {
                PremiumDialogManager.showPremiumInfoDialog(requireContext(), settingsViewModel);
            }
        });

        binding.languageChangeButton.setOnClickListener(v -> {
            showLanguageSelectionDialog();
        });

        binding.themeChangeButton.setOnClickListener(v -> {
            if (settingsViewModel.userRole.getValue() == UserRole.PREMIUM) {
                showThemeSelectionDialog();
            } else {
                PremiumDialogManager.showPremiumInfoDialog(requireContext(), settingsViewModel);
            }
        });

        binding.aboutButton.setOnClickListener(v -> {
            handleAboutAndSecretClick();
        });
    }

    private void showLanguageSelectionDialog() {
        String[] languageCodes = { "system", "ru", "en" };
        String[] items = { getString(R.string.settings_system_defaults),
                getString(R.string.lang_ru), getString(R.string.lang_en) };

        String currentCode = settingsViewModel.languageCode.getValue();
        int checkedItem = 0;
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(currentCode)) {
                checkedItem = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_dialog_title_language)
                .setSingleChoiceItems(items, checkedItem, (dialog, i) -> {
                    settingsViewModel.changeLanguage(languageCodes[i]);
                    dialog.dismiss();
                }).show();
    }

    private void showCurrencySelectionDialog() {
        Currency[] currencies = Currency.values();

        String[] items = new String[currencies.length];
        for (int i = 0; i < currencies.length; i++) {
            items[i] = currencies[i].getCode() + " (" + currencies[i].getSymbol() + ")";
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_dialog_title_currency)
                .setItems(items, (dialog, i) -> {
                    Currency selectedCurrency = currencies[i];
                    settingsViewModel.changeCurrency(selectedCurrency);
                }).show();
    }

    private void showThemeSelectionDialog() {
        int[] themeModes = { -1, 1, 2 };
        String[] items = { getString(R.string.settings_system_defaults),
                getString(R.string.theme_light), getString(R.string.theme_dark) };

        int currentMode = settingsViewModel.themeMode.getValue() != null
                ? settingsViewModel.themeMode.getValue()
                : -1;

        int checkedItem = 0;
        for (int i = 0; i < themeModes.length; i++) {
            if (themeModes[i] == currentMode) {
                checkedItem = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_dialog_title_theme)
                .setSingleChoiceItems(items, checkedItem, (dialog, i) -> {
                   settingsViewModel.changeThemeMode(themeModes[i]);
                   dialog.dismiss();
                }).show();
    }

    private void showAboutDialog() {
        PackageInfo packageInfo;

        try {
            PackageManager pm = getContext().getPackageManager();
            String packageName = getContext().getPackageName();
            packageInfo = pm.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String message = getString(R.string.settings_dialog_about_message, packageInfo.versionName,
                getDeveloperName());

        new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.settings_about_app)
                .setMessage(message)
                .setPositiveButton("ОК", null)
                .show();
    }

    private String getDeveloperName() {
        try {
            ApplicationInfo ai = getContext().getPackageManager()
                    .getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);

            Bundle bundle = ai.metaData;
            return bundle.getString("developer_name");
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private void handleAboutAndSecretClick() {
        if (clickRunnable != null) {
            clickHandler.removeCallbacks(clickRunnable);
        }

        premiumTapCount++;

        if (premiumTapCount == 5) {
            premiumTapCount = 0;
            clickRunnable = null;
            showSecretPremiumDialog();
        } else {
            clickRunnable = () -> {
                premiumTapCount = 0;
                showAboutDialog();
            };
            clickHandler.postDelayed(clickRunnable, 400);
        }
    }

    private void showSecretPremiumDialog() {
        String[] options = {
                getString(R.string.secret_role_regular),
                getString(R.string.secret_role_premium)
        };

        UserRole currentRole = settingsViewModel.userRole.getValue();
        int checkedItem = (currentRole == UserRole.PREMIUM) ? 1 : 0;

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.secret_dialog_title))
                .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                    UserRole selectedRole = (which == 1) ? UserRole.PREMIUM : UserRole.REGULAR;

                    settingsViewModel.changeUserRole(selectedRole);

                    String message = (selectedRole == UserRole.PREMIUM)
                            ? getString(R.string.premium_dialog_success_toast)
                            : getString(R.string.secret_toast_regular_activated);
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.secret_dialog_btn_cancel), null)
                .show();
    }
}