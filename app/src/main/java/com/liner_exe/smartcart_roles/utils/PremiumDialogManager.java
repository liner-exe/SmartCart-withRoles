package com.liner_exe.smartcart_roles.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.liner_exe.domain.enums.UserRole;
import com.liner_exe.smartcart_roles.R;
import com.liner_exe.smartcart_roles.viewmodel.SettingsViewModel;

public class PremiumDialogManager {
    public static void showPremiumInfoDialog(Context context, SettingsViewModel settingsViewModel) {
        String[] premiumFeatures = {
                "⭐ " + context.getString(R.string.premium_feature_analytics),
                "📊 " + context.getString(R.string.premium_feature_charts),
                "✨ " + context.getString(R.string.premium_feature_unlimited),
                "🛒 " + context.getString(R.string.premium_feature_stores),
                "💱 " + context.getString(R.string.premium_feature_currency),
                "🎨 " + context.getString(R.string.premium_feature_themes)
        };

        new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.premium_dialog_title))
                .setItems(premiumFeatures, null)
                .setPositiveButton(context.getString(R.string.premium_dialog_btn_activate), (dialog, which) -> {
                    dialog.dismiss();
                    if (settingsViewModel != null) {
                        settingsViewModel.changeUserRole(UserRole.PREMIUM);
                        Toast.makeText(context, context.getString(R.string.premium_dialog_success_toast), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(context.getString(R.string.premium_dialog_btn_close), null)
                .show();
    }
}
