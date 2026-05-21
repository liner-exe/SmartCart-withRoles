package com.liner_exe.data.mapper;

import com.liner_exe.data.local.dto.ShoppingListWithProgressDto;
import com.liner_exe.data.local.entities.ShoppingListEntity;
import com.liner_exe.domain.models.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListMapper {
    public static ShoppingList toModel(ShoppingListWithProgressDto dto) {
        return new ShoppingList(
                dto.id,
                dto.name,
                dto.totalItems,
                dto.boughtItems,
                dto.createdAt
        );
    }

    public static ShoppingListEntity toEntity(ShoppingList model) {
        return new ShoppingListEntity(
                model.getId(),
                model.getName(),
                model.getCreatedAt()
        );
    }

    public static List<ShoppingList> toModelList(List<ShoppingListWithProgressDto> dtos) {
        List<ShoppingList> models = new ArrayList<>();
        for (ShoppingListWithProgressDto dto : dtos) {
            models.add(toModel(dto));
        }
        return models;
    }
}
