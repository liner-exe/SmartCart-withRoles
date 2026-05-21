package com.liner_exe.data.local.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String emoji;

    public CategoryEntity() {

    }

    @Ignore
    public CategoryEntity(int id, String name, String emoji) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
    }
}
