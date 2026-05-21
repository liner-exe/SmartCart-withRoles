package com.liner_exe.domain.models;

import java.util.Objects;

public class DateHeader implements DisplayItem {
    private final String date;

    public DateHeader(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getViewType() {
        return TYPE_DATE_HEADER;
    }

    @Override
    public int getId() {
        return date.hashCode();
    }

    @Override
    public boolean isContentTheSame(Object other) {
        if (!(other instanceof DateHeader)) return false;

        DateHeader that = (DateHeader) other;
        return Objects.equals(this.date, that.date);
    }
}
