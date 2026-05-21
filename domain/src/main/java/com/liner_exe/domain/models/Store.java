package com.liner_exe.domain.models;

import java.io.Serializable;
import java.util.Objects;

public class Store implements DiffIdentifiable {
    private final int id;
    private final String name;

    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Store(String name) {
        this.name = name;
        this.id = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isContentTheSame(Object other) {
        return this.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store that = (Store) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
