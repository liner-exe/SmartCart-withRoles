package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.liner_exe.domain.models.Category;
import com.liner_exe.smartcart.databinding.ItemCategoryBinding;

public class CategorySelectionAdapter extends BaseAdapter<Category, ItemCategoryBinding> {
    @NonNull
    @Override
    public BaseViewHolder<ItemCategoryBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemCategoryBinding binding, Category item) {
        binding.categoryItemTitle.setText(item.getName());
        binding.categoryItemEmoji.setText(item.getEmoji());
        binding.categoryItemButtonMore.setVisibility(View.GONE);
    }
}
