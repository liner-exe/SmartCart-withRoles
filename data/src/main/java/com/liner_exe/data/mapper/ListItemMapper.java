package com.liner_exe.data.mapper;

import com.liner_exe.data.local.dto.ListItemDto;
import com.liner_exe.data.local.dto.monitoring.CategoryProgressDto;
import com.liner_exe.data.local.dto.monitoring.StoreProgressDto;
import com.liner_exe.data.local.entities.ListItemEntity;
import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.models.monitoring.CategoryProgress;
import com.liner_exe.domain.models.monitoring.StoreProgress;

import java.util.ArrayList;
import java.util.List;

public class ListItemMapper {
    public static ListItem toModel(ListItemDto dto) {
        Category category = null;
        if (dto.categoryId != null) {
            category = new Category(dto.categoryId, dto.categoryName, dto.categoryEmoji);
        }

        return new ListItem(
                dto.id,
                new Product(dto.productId, dto.productName, dto.categoryId, category),
                dto.quantity,
                dto.price,
                dto.unit,
                dto.isChecked,
                dto.listId,
                dto.storeId
        );
    }

    public static ListItemEntity toEntity(ListItem listItem) {
        return new ListItemEntity(
            listItem.getId(),
            listItem.getListId(),
            listItem.getProduct().getId(),
            listItem.getStoreId(),
            listItem.getQuantity(),
            listItem.getPrice(),
            listItem.getUnit(),
            listItem.isBought()
        );
    }

    public static List<ListItem> toModelList(List<ListItemDto> dtos) {
        List<ListItem> listItems = new ArrayList<>();
        for (ListItemDto dto : dtos) {
            listItems.add(toModel(dto));
        }

        return listItems;
    }

    public static List<CategoryProgress> toCategoryProgressList(List<CategoryProgressDto> dtos) {
        List<CategoryProgress> result = new ArrayList<>();
        if (dtos == null) return result;

        for (CategoryProgressDto dto : dtos) {
            result.add(new CategoryProgress(
                    dto.categoryName, dto.categoryEmoji, dto.totalExpense
            ));
        }

        return result;
    }

    public static List<StoreProgress> toStoreProgressList(List<StoreProgressDto> dtos) {
        List<StoreProgress> result = new ArrayList<>();

        for (StoreProgressDto dto : dtos) {
            result.add(new StoreProgress(
                    dto.storeName, dto.totalExpense
            ));
        }

        return result;
    }
}
