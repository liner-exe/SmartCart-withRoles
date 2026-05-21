package com.liner_exe.domain.utils;

public class QuantityCalculator {
    private static final double SMALL_STEP = 0.1;
    private static final double LARGE_STEP = 1.0;

    public static double getStep(boolean decimalMode) {
        return decimalMode ? SMALL_STEP : LARGE_STEP;
    }

    public static double increment(double value, boolean decimalMode) {
        return value + getStep(decimalMode);
    }

    public static double decrement(double value, boolean decimalMode) {
        double result = value - getStep(decimalMode);
        return result < 0 ? 0 : result;
    }
}
