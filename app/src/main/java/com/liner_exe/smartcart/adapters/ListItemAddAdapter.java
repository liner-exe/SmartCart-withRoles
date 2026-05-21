package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.liner_exe.domain.models.Product;
import com.liner_exe.smartcart.databinding.ItemProductBinding;

public class ListItemAddAdapter extends BaseAdapter<Product, ItemProductBinding> {
    @NonNull
    @Override
    public BaseViewHolder<ItemProductBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemProductBinding binding, Product product) {
        binding.productItemTitle.setText(product.getName());

        binding.productItemButtonMore.setVisibility(View.GONE);
    }
}
