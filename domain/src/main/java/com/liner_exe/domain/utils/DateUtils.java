package com.liner_exe.domain.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatDate(long timestamp) {
        return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date(timestamp));
    }
}
