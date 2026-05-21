package com.liner_exe.smartcart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.models.Store;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.StoreAdapter;
import com.liner_exe.smartcart.databinding.FragmentStoresManagementBinding;
import com.liner_exe.smartcart.dialogs.ProductDialogFragment;
import com.liner_exe.smartcart.dialogs.StoreDialogFragment;
import com.liner_exe.smartcart.viewmodel.StoresViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoresManagementFragment extends Fragment {
    private FragmentStoresManagementBinding binding;
    private StoreAdapter adapter;
    private StoresViewModel storesViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoresManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storesViewModel = new ViewModelProvider(requireActivity()).get(StoresViewModel.class);

        setupToolbar();
        setupSearchLogic();
        setupRecyclerView();
        observeViewModel();

        bindDialog();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new StoreAdapter(new StoreAdapter.OnStoreActionListener() {
            @Override
            public void onRename(Store store) {
                StoreDialogFragment.newInstance(store.getName(), newName -> {
                    Store updatedStore = new Store(store.getId(), newName);
                    storesViewModel.updateStore(updatedStore);
                }).show(getChildFragmentManager(), "RenameProductDialog");
            }

            @Override
            public void onDelete(Store store) {
                storesViewModel.deleteStoreById(store.getId());
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        storesViewModel.isDbEmpty.observe(getViewLifecycleOwner(), isEmpty -> {
            binding.searchInputLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        storesViewModel.filteredStores.observe(getViewLifecycleOwner(), stores -> {
            adapter.setItems(stores);

            boolean isQueryEmpty = binding.searchEditText.getText().toString().trim().isEmpty();
            updateVisibilityUI(stores == null || stores.isEmpty(), !isQueryEmpty);
        });
    }

    private void bindDialog() {
        binding.fab.setOnClickListener(v -> {
            StoreDialogFragment.newInstance(null, name -> {
                storesViewModel.addStore(new Store(name));
            }).show(getChildFragmentManager(), "AddStoreDialog");
        });
    }

    private void setupSearchLogic() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                storesViewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void updateVisibilityUI(boolean isEmpty, boolean isFromSearch) {
        if (isEmpty) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyStateView.setVisibility(View.VISIBLE);
            updateScrollFlags(true);

            if (isFromSearch) {
                binding.emptyStateTitle.setText(R.string.search_nothing_found_title);
                binding.emptyStateMessage.setText(R.string.search_nothing_found_message);
            } else {
                binding.emptyStateTitle.setText(R.string.sm_empty_list_title);
                binding.emptyStateMessage.setText(R.string.sm_empty_list_message);
            }
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyStateView.setVisibility(View.GONE);
            updateScrollFlags(false);
        }
    }

    private void updateScrollFlags(boolean isEmpty) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.collapsingToolbar.getLayoutParams();
        if (isEmpty) {
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        }
        binding.collapsingToolbar.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        storesViewModel.setSearchQuery("");
    }
}