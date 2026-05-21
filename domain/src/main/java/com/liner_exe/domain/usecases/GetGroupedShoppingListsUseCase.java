package com.liner_exe.domain.usecases;

import com.liner_exe.domain.models.DateHeader;
import com.liner_exe.domain.models.DisplayItem;
import com.liner_exe.domain.models.ShoppingList;
import com.liner_exe.domain.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class GetGroupedShoppingListsUseCase {
    public List<DisplayItem> execute(List<ShoppingList> lists, String query) {
        if (lists == null) return new ArrayList<>();

        List<ShoppingList> filtered = filter(lists, query);

        return group(filtered);
    }

    private List<ShoppingList> filter(List<ShoppingList> lists, String query) {
        if (query == null || query.isEmpty()) return lists;

        List<ShoppingList> result = new ArrayList<>();
        String pattern = query.toLowerCase().trim();
        for (ShoppingList item : lists) {
            if (item.getName().toLowerCase().contains(pattern)) {
                result.add(item);
            }
        }

        return result;
    }

    private List<DisplayItem> group(List<ShoppingList> lists) {
        List<DisplayItem> result = new ArrayList<>();
        String lastDate = "";

        for (ShoppingList list : lists) {
            String currentDate = DateUtils.formatDate(list.getCreatedAt());

            if (!currentDate.equals(lastDate)) {
                result.add(new DateHeader(currentDate));
                lastDate = currentDate;
            }
            result.add(list);
        }
        return result;
    }
}
