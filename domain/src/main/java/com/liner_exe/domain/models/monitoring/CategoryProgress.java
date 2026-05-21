package com.liner_exe.domain.models.monitoring;

public class CategoryProgress {
    private final String name;
    private final String emoji;
    private final double totalExpense;

    public CategoryProgress(String name, String emoji, double totalExpense) {
        this.name = name;
        this.emoji = emoji;
        this.totalExpense = totalExpense;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public double getTotalExpense() {
        return totalExpense;
    }
}