package com.liner_exe.domain.models;

public interface DiffIdentifiable {
    int getId();

    boolean isContentTheSame(Object other);
}
