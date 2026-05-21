package com.liner_exe.smartcart.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Window;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class ThemeUtils {
    @ColorInt
    public static int getThemeColor(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return typedValue.data;
    }

    public static void setSystemBarsFromAttributes(Window window, @AttrRes int statusBarAttr, @AttrRes int navigationBarAttr) {
        if (window == null) return;

        Context context = window.getContext();
        int statusBarColor = getColorFromAttribute(context, statusBarAttr);
        int navigationBarColor = getColorFromAttribute(context, navigationBarAttr);

        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(navigationBarColor);

        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
        controller.setAppearanceLightStatusBars(isColorLight(statusBarColor));
        controller.setAppearanceLightNavigationBars(isColorLight(navigationBarColor));
    }

    @ColorInt
    public static int getColorFromAttribute(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.data;
        }
        return Color.TRANSPARENT;
    }

    private static boolean isColorLight(@ColorInt int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }
}
