package com.liner_exe.smartcart.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utils {
    public static int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * displayMetrics.density);
    }
}
