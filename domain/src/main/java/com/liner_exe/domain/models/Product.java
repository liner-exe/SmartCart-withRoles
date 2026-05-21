package com.liner_exe.domain.models;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable, DiffIdentifiable {
    private final int id;
    private final String name;
    private final Integer categoryId;
    private final Category category;

    public Product(int id, String name, Integer categoryId, Category category) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.category = category;
    }

    public Product(int id, String name, Integer categoryId) {
        this(id, name, categoryId, null);
    }

    public Product(String name) {
        this(0, name, null, null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean isContentTheSame(Object other) {
        return this.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(categoryId, product.categoryId) &&
                Objects.equals(name, product.name) &&
                Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categoryId, category);
    }
}
