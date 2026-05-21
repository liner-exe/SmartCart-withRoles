package com.liner_exe.smartcart.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.liner_exe.domain.models.DiffIdentifiable;

import java.util.Collections;
import java.util.List;

public abstract class BaseAdapter<T extends DiffIdentifiable, VB extends ViewBinding>
        extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>> {
    protected OnItemClickListener<T> onItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public void setItems(List<T> items) {
        this.listDiffer.submitList(items != null ? items : Collections.emptyList());
    }

    @Override
    public int getItemCount() {
        return this.listDiffer.getCurrentList().size();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<VB> holder, int position) {
        T item = listDiffer.getCurrentList().get(position);
        bind(holder.binding, item);

        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (onItemClickListener != null && currentPos != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(listDiffer.getCurrentList().get(currentPos), currentPos);
            }
        });
    }

    protected abstract void bind(VB binding, T item);

    public static class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
        public final VB binding;

        public BaseViewHolder(@NonNull VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private final AsyncListDiffer<T> listDiffer = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return oldItem.isContentTheSame(newItem);
        }
    });
}
