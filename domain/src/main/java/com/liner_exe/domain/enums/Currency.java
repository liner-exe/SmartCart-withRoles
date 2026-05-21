package com.liner_exe.domain.enums;

public enum Currency {
    DOLLAR("$", "USD"),
    EURO("€", "EUR"),
    RUB("₽", "RUB");

    private final String symbol;
    private final String code;

    Currency(String symbol, String code) {
        this.symbol = symbol;
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCode() {
        return code;
    }

    public static Currency fromCode(String code) {
        for (Currency c : values()) {
            if (c.code.equalsIgnoreCase(code)) return c;
        }
        return DOLLAR;
    }

    public String format(double amount) {
        if (this == DOLLAR) {
            return String.format("%s %.2f", symbol, amount);
        } else {
            return String.format("%.2f %s", amount, symbol);
        }
    }
}