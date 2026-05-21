package com.liner_exe.data.local.dto;

import androidx.room.ColumnInfo;

public class ShoppingListWithProgressDto {
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "totalItems")
    public int totalItems;

    @ColumnInfo(name = "boughtItems")
    public int boughtItems;

    @ColumnInfo(name = "createdAt")
    public long createdAt;
}
