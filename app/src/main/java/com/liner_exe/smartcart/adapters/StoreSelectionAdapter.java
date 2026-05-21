package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.liner_exe.domain.models.Store;
import com.liner_exe.smartcart.databinding.ItemProductBinding;

public class StoreSelectionAdapter extends BaseAdapter<Store, ItemProductBinding> {
    @NonNull
    @Override
    public BaseViewHolder<ItemProductBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemProductBinding binding, Store store) {
        binding.productItemTitle.setText(store.getName());
        binding.productItemButtonMore.setVisibility(View.GONE);
    }
}
