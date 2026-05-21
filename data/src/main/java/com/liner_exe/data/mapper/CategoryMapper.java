package com.liner_exe.data.mapper;

import com.liner_exe.data.local.entities.CategoryEntity;
import com.liner_exe.domain.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static Category toModel(CategoryEntity entity) {
        if (entity == null) return null;

        return new Category(
                entity.id,
                entity.name,
                entity.emoji
        );
    }

    public static CategoryEntity toEntity(Category model) {
        if (model == null) return null;

        return new CategoryEntity(
                model.getId(),
                model.getName(),
                model.getEmoji()
        );
    }

    public static List<Category> toModelList(List<CategoryEntity> entities) {
        List<Category> models = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            models.add(toModel(entity));
        }
        return models;
    }
}
