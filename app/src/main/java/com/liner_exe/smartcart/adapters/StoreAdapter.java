package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.models.Store;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.ItemProductBinding;

public class StoreAdapter extends BaseAdapter<Store, ItemProductBinding> {
    public interface OnStoreActionListener {
        void onRename(Store store);
        void onDelete(Store store);
    }

    private final OnStoreActionListener listener;

    public StoreAdapter(OnStoreActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemProductBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    private void showPopupMenu(View view, Store store) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.store_context_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.action_rename_store) {
                listener.onRename(store);
                return true;
            } else if (id == R.id.action_delete_store) {
                listener.onDelete(store);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    @Override
    protected void bind(ItemProductBinding binding, Store store) {
        binding.productItemTitle.setText(store.getName());
        binding.productItemButtonMore.setOnClickListener(v -> showPopupMenu(v, store));
    }
}