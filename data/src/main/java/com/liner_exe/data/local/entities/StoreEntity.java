package com.liner_exe.data.local.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "stores")
public class StoreEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public StoreEntity() {

    }

    @Ignore
    public StoreEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
