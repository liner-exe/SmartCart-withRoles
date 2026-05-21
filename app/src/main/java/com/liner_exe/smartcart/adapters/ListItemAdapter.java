package com.liner_exe.smartcart.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.domain.utils.formatters.QuantityFormatter;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.ItemListBinding;

import java.util.List;


public class ListItemAdapter extends BaseAdapter<ListItem, ItemListBinding> {
    public interface OnListItemActionListener {
        void onCheckbox(ListItem listItem);
        void onEdit(ListItem listItem);
        void onDelete(ListItem listItem);
    }

    private final OnListItemActionListener listener;

    public ListItemAdapter(OnListItemActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemListBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListBinding binding = ItemListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BaseViewHolder<>(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bind(ItemListBinding binding, ListItem item) {
        binding.textProductName.setText(item.getProduct().getName());
        binding.textQuantity.setText(QuantityFormatter.format(item.getQuantity()) + " " + item.getUnit());
        binding.checkboxItem.setChecked(item.isBought());
        binding.checkboxItem.setOnClickListener(v -> {
            listener.onCheckbox(item);
        });

        bindCategoryDisplay(binding, item);

        binding.buttonMore.setOnClickListener(v -> showPopup(v, item));
    }

    private void bindCategoryDisplay(ItemListBinding binding, ListItem listItem) {
        if (listItem.getProduct().getCategory() != null) {
            Category category = listItem.getProduct().getCategory();
            String name = category.getName();
            String emoji = category.getEmoji();

            if (name != null && !name.isEmpty() && emoji != null && !emoji.isEmpty()) {
                String fullText = emoji + " " + name;
                binding.textCategory.setText(fullText);
                binding.textCategory.setVisibility(View.VISIBLE);
            } else {
                binding.textCategory.setVisibility(View.GONE);
            }
        }  else {
            binding.textCategory.setVisibility(View.GONE);
        }
    }

    private void showPopup(View view, ListItem listItem) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.list_item_context_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_edit_list_item) {
                listener.onEdit(listItem);
                return true;
            } else if (id == R.id.action_delete_list_item) {
                listener.onDelete(listItem);
                return true;
            }

            return false;
        });

        popup.show();
    }
}