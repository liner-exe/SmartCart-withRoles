package com.liner_exe.data.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity(tableName = "shopping_lists")
public class ShoppingListEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public long createdAt = System.currentTimeMillis();

    public ShoppingListEntity() {

    }

    @Ignore
    public ShoppingListEntity(int id, String name, long createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
