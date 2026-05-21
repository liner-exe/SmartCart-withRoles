package com.liner_exe.smartcart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import static com.google.android.material.R.attr;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.StoreSelectionAdapter;
import com.liner_exe.smartcart.databinding.FragmentStoreSelectionBinding;
import com.liner_exe.smartcart.utils.ThemeUtils;
import com.liner_exe.smartcart.viewmodel.ListItemsViewModel;
import com.liner_exe.smartcart.viewmodel.StoresViewModel;

public class StoreSelectionFragment extends Fragment {
    private FragmentStoreSelectionBinding binding;
    private StoreSelectionAdapter adapter;
    private StoresViewModel storesViewModel;
    private ListItemsViewModel listItemsViewModel;
    private ListItem listItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoreSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storesViewModel = new ViewModelProvider(requireActivity()).get(StoresViewModel.class);
        listItemsViewModel = new ViewModelProvider(requireActivity()).get(ListItemsViewModel.class);

        handleArguments();

        setupToolbar();
        setupSearchLogic();
        setupRecyclerView();
        observeViewModel();
    }

    private void handleArguments() {
        if (getArguments() != null) {
            StoreSelectionFragmentArgs args = StoreSelectionFragmentArgs.fromBundle(getArguments());
            listItem = args.getListItem();
        }
    }

    private void setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.store_selection_menu);
        MenuItem itemMore = binding.toolbar.getMenu().findItem(R.id.action_more);

        if (itemMore != null && itemMore.getIcon() != null) {
            itemMore.getIcon().mutate().setTint(
                    ThemeUtils.getThemeColor(requireContext(), attr.colorOnSurface)
            );
        }

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_reset_store) {
                resetStore();
                return true;
            }
            return false;
        });

        binding.toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new StoreSelectionAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((store, position) -> {
            storesViewModel.setSelectedStore(store);
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
                storesViewModel.setSearchQuery(s.toString());
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
                binding.emptyStateTitle.setText(R.string.ss_empty_list_title);
                binding.emptyStateMessage.setText(R.string.ss_empty_list_message);
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

    private void resetStore() {
        storesViewModel.setSelectedStore(null);
        NavHostFragment.findNavController(this).popBackStack();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        storesViewModel.setSearchQuery("");
    }
}