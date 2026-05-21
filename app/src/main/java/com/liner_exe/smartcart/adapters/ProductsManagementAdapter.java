package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liner_exe.domain.models.Product;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.ItemCategoryBinding;
import com.liner_exe.smartcart.databinding.ItemProductBinding;

import java.util.List;

public class ProductsManagementAdapter
        extends BaseAdapter<Product, ItemProductBinding> {
    public interface OnProductActionListener {
        void onEdit(Product product);
        void onRename(Product product);
        void onDelete(Product product);
    }

    private final OnProductActionListener listener;

    public ProductsManagementAdapter(OnProductActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemProductBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BaseViewHolder<>(binding);
    }

    private void showPopupMenu(View view, Product product) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.product_context_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.action_edit_product) {
                listener.onEdit(product);
                return true;
            } else if (id == R.id.action_rename_product) {
                listener.onRename(product);
                return true;
            } else if (id == R.id.action_delete_product) {
                listener.onDelete(product);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    @Override
    protected void bind(ItemProductBinding binding, Product product) {
        binding.productItemTitle.setText(product.getName());
        binding.productItemButtonMore.setOnClickListener(v -> showPopupMenu(v, product));
    }
}