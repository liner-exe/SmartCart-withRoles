package com.liner_exe.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "products",
        foreignKeys = @ForeignKey(
                entity = CategoryEntity.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.SET_NULL
        )
)
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public Integer categoryId;

    public ProductEntity() {

    }

    @Ignore
    public ProductEntity(int id, String name, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }
}
