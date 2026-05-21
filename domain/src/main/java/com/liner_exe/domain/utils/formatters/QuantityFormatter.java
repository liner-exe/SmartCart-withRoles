package com.liner_exe.domain.utils.formatters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class QuantityFormatter {
    public static String format(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

        DecimalFormat df = new DecimalFormat("0.###", symbols);

        return df.format(value);
    }

    public static double parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
