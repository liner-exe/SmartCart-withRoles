package com.liner_exe.data.mapper;

import com.liner_exe.data.local.entities.ProductEntity;
import com.liner_exe.domain.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static Product toModel(ProductEntity entity) {
        if (entity == null) return null;

        return new Product(
            entity.id,
            entity.name,
            entity.categoryId
        );
    }

    public static ProductEntity toEntity(Product model) {
        if (model == null) return null;

        return new ProductEntity(
            model.getId(),
            model.getName(),
            model.getCategoryId()
        );
    }

    public static List<Product> toModelList(List<ProductEntity> entities) {
        List<Product> models = new ArrayList<>();
        for (ProductEntity entity : entities) {
            models.add(toModel(entity));
        }

        return models;
    }
}
