package com.liner_exe.smartcart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.ListItemAddAdapter;
import com.liner_exe.smartcart.databinding.FragmentListItemAddBinding;
import com.liner_exe.smartcart.viewmodel.ProductViewModel;
import com.liner_exe.smartcart.viewmodel.ListItemsViewModel;


public class ListItemAddFragment extends Fragment {
    private FragmentListItemAddBinding binding;
    private ListItemAddAdapter adapter;
    private ProductViewModel productViewModel;
    private ListItemsViewModel listItemsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListItemAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        listItemsViewModel = new ViewModelProvider(requireActivity()).get(ListItemsViewModel.class);

        setupToolbar();
        setupSearchLogic();
        setupRecyclerView();
        observeViewModel();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new ListItemAddAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((product, position) -> {
            listItemsViewModel.addProductAsListItem(product, getString(R.string.list_item_edit_default_unit));
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupSearchLogic() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                productViewModel.setSearchQuery(s.toString());
            }
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
                binding.emptyStateTitle.setText(R.string.ps_empty_list_title);
                binding.emptyStateMessage.setText(R.string.ps_empty_list_message);
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

    private void observeViewModel() {
        productViewModel.isDbEmpty.observe(getViewLifecycleOwner(), isEmpty -> {
            binding.searchInputLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        productViewModel.filteredProducts.observe(getViewLifecycleOwner(), products -> {
            adapter.setItems(products);

            boolean isQueryEmpty = binding.searchEditText.getText().toString().trim().isEmpty();
            updateVisibilityUI(products == null || products.isEmpty(), !isQueryEmpty);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        productViewModel.setSearchQuery("");
    }
}