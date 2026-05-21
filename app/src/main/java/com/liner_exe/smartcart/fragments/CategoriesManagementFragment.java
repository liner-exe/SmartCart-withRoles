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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.Category;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.CategoriesManagementAdapter;
import com.liner_exe.smartcart.databinding.FragmentCategoriesManagementBinding;
import com.liner_exe.smartcart.viewmodel.CategoryViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoriesManagementFragment extends Fragment {
    private FragmentCategoriesManagementBinding binding;
    private CategoriesManagementAdapter adapter;
    private CategoryViewModel categoryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoriesManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        setupToolbar();
        setupSearchLogic();
        setupRecyclerView();
        observeViewModel();
        setupFab();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new CategoriesManagementAdapter(new CategoriesManagementAdapter.OnCategoryActionListener() {
            @Override
            public void onDelete(Category category) {
                categoryViewModel.deleteCategoryById(category.getId());
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((category, position) -> navigateToEdit(category));
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

    private void setupFab() {
        binding.fab.setOnClickListener(v -> navigateToEdit(null));
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
                binding.emptyStateTitle.setText(R.string.cm_empty_list_title);
                binding.emptyStateMessage.setText(R.string.cm_empty_list_message);
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

    private void navigateToEdit(@Nullable Category category) {
        NavDirections action = CategoriesManagementFragmentDirections
                .actionCategoryManagementFragmentToCategoryEditFragment()
                .setCategory(category);
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryViewModel.setSearchQuery("");
    }
}