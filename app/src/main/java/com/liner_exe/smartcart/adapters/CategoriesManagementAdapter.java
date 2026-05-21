package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import com.liner_exe.domain.models.Category;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.ItemCategoryBinding;

public class CategoriesManagementAdapter extends BaseAdapter<Category, ItemCategoryBinding> {
    public interface OnCategoryActionListener {
        void onDelete(Category category);
    }

    private final OnCategoryActionListener listener;

    public CategoriesManagementAdapter(OnCategoryActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemCategoryBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BaseViewHolder<>(binding);
    }

    void showPopupMenu(View view, Category category) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.category_context_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_delete_category) {
                listener.onDelete(category);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    @Override
    protected void bind(ItemCategoryBinding binding, Category category) {
        binding.categoryItemTitle.setText(category.getName());
        binding.categoryItemEmoji.setText(category.getEmoji());

        binding.categoryItemButtonMore.setOnClickListener(v -> {
            showPopupMenu(v, category);
        });
    }
}