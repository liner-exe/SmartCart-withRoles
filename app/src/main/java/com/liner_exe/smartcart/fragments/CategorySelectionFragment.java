package com.liner_exe.smartcart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.Product;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.CategorySelectionAdapter;
import com.liner_exe.smartcart.databinding.FragmentCategorySelectionBinding;
import com.liner_exe.smartcart.utils.ThemeUtils;
import com.liner_exe.smartcart.viewmodel.CategoryViewModel;

public class CategorySelectionFragment extends Fragment {
    private FragmentCategorySelectionBinding binding;
    private CategorySelectionAdapter adapter;
    private CategoryViewModel categoryViewModel;
    private Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategorySelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        handleArguments();

        setupToolbar();
        setupSearchLogic();
        setupRecyclerView();
        observeViewModel();
    }

    private void handleArguments() {
        if (getArguments() != null) {
            CategorySelectionFragmentArgs args = CategorySelectionFragmentArgs.fromBundle(getArguments());
            product = args.getProduct();
        }
    }

    private void setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.category_selection_menu);
        MenuItem itemMore = binding.toolbar.getMenu().findItem(R.id.action_more);

        if (itemMore != null && itemMore.getIcon() != null) {
            itemMore.getIcon().mutate().setTint(
                    ThemeUtils.getThemeColor(requireContext(), com.google.android.material.R.attr.colorOnSurface)
            );
        }

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_reset_category) {
                resetCategory();
                return true;
            }

            return false;
        });

        binding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new CategorySelectionAdapter();

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnItemClickListener((category, position) -> {
            categoryViewModel.setSelectedCategory(category);
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void setupSearchLogic() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                categoryViewModel.setSearchQuery(s.toString());
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
                binding.emptyStateTitle.setText(R.string.cs_empty_list_title);
                binding.emptyStateMessage.setText(R.string.cs_empty_list_message);
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

    private void resetCategory() {
        categoryViewModel.resetSelectedCategory();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void observeViewModel() {
        categoryViewModel.isDbEmpty.observe(getViewLifecycleOwner(), isEmpty -> {
            binding.searchInputLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        categoryViewModel.filteredCategories.observe(getViewLifecycleOwner(), categories -> {
            adapter.setItems(categories);

            boolean isQueryEmpty = binding.searchEditText.getText().toString().trim().isEmpty();
            updateVisibilityUI(categories == null || categories.isEmpty(), !isQueryEmpty);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryViewModel.setSearchQuery("");
    }
}