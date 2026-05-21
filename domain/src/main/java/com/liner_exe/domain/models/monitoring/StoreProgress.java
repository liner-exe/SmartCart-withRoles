package com.liner_exe.domain.models.monitoring;

public class StoreProgress {
    private final String storeName;
    private final double totalExpense;

    public StoreProgress(String storeName, double totalExpense) {
        this.storeName = storeName;
        this.totalExpense = totalExpense;
    }

    public String getStoreName() {
        return storeName;
    }

    public double getTotalExpense() {
        return totalExpense;
    }
}
