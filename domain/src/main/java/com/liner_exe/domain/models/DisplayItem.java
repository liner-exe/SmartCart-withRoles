package com.liner_exe.domain.models;

public interface DisplayItem extends DiffIdentifiable {
    int TYPE_DATE_HEADER = 0;
    int TYPE_SHOPPING_LIST = 1;

    int getViewType();
}
