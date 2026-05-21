package com.liner_exe.data.mapper;

import com.liner_exe.data.local.entities.StoreEntity;
import com.liner_exe.domain.models.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreMapper {
    public static Store toModel(StoreEntity entity) {
        if (entity == null) return null;

        return new Store(
            entity.id,
            entity.name
        );
    }

    public static StoreEntity toEntity(Store model) {
        if (model == null) return null;

        return new StoreEntity(
            model.getId(),
            model.getName()
        );
    }

    public static List<Store> toModelList(List<StoreEntity> entities) {
        List<Store> models = new ArrayList<>();
        for (StoreEntity entity : entities) {
            models.add(toModel(entity));
        }

        return models;
    }
}
