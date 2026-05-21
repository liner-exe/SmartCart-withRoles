package com.liner_exe.smartcart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.liner_exe.domain.models.DateHeader;
import com.liner_exe.domain.models.DisplayItem;
import com.liner_exe.domain.models.ShoppingList;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.CardShoppingListBinding;
import com.liner_exe.smartcart.databinding.ItemDateHeaderBinding;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DATE = DisplayItem.TYPE_DATE_HEADER;
    private static final int TYPE_LIST = DisplayItem.TYPE_SHOPPING_LIST;

    private List<DisplayItem> items = new ArrayList<>();
    private final OnShoppingListActionListener listener;
    private OnItemClickListener onItemClickListener;

    private final AsyncListDiffer<DisplayItem> listDiffer = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull DisplayItem oldItem, @NonNull DisplayItem newItem) {
            if (oldItem.getViewType() != newItem.getViewType()) return false;

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DisplayItem oldItem, @NonNull DisplayItem newItem) {
            return oldItem.isContentTheSame(newItem);
        }
    });

    public ShoppingListsAdapter(OnShoppingListActionListener listener) {
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setItems(List<DisplayItem> newItems) {
        listDiffer.submitList(newItems);
    }

    @Override
    public int getItemViewType(int position) {
        return listDiffer.getCurrentList().get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DisplayItem.TYPE_DATE_HEADER) {
            ItemDateHeaderBinding binding = ItemDateHeaderBinding.inflate(inflater, parent, false);
            return new DateViewHolder(binding);
        } else {
            CardShoppingListBinding binding = CardShoppingListBinding.inflate(inflater, parent, false);
            return new ShoppingListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DisplayItem item = listDiffer.getCurrentList().get(position);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int currentPos = holder.getBindingAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(listDiffer.getCurrentList().get(currentPos),
                            currentPos);
                }
            }
        });

        if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).bind((DateHeader) item);
        } else if (holder instanceof ShoppingListViewHolder) {
            ((ShoppingListViewHolder) holder).bind((ShoppingList) item, listener);
        }
    }

    @Override
    public int getItemCount() {
        return listDiffer.getCurrentList().size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        private final ItemDateHeaderBinding binding;

        public DateViewHolder(ItemDateHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DateHeader header) {
            binding.tvDateHeader.setText(header.getDate());
        }
    }

    static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        private final CardShoppingListBinding binding;

        public ShoppingListViewHolder(CardShoppingListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        protected void bind(ShoppingList list, OnShoppingListActionListener listener) {
            binding.shoppingListName.setText(list.getName());
            binding.shoppingListProgressText.setText(list.getProgressString());
            binding.shoppingListButtonMore.setOnClickListener(v -> showPopupMenu(v, list, listener));

            binding.shoppingListProgressBar.setProgress(list.getBoughtItems());
            binding.shoppingListProgressBar.setMax(list.getTotalItems());
        }

        private void showPopupMenu(View view, ShoppingList shoppingList, OnShoppingListActionListener listener) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.list_context_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.action_rename) {
                    listener.onRename(shoppingList);
                    return true;
                } else if (id == R.id.action_delete_list) {
                    listener.onDelete(shoppingList);
                    return true;
                }

                return false;
            });

            popupMenu.show();
        }
    }

    public interface OnShoppingListActionListener {
        void onRename(ShoppingList shoppingList);

        void onDelete(ShoppingList shoppingList);
    }

    public interface OnItemClickListener {
        void onItemClick(DisplayItem item, int position);
    }
}
