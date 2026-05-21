package com.liner_exe.smartcart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.liner_exe.domain.enums.Currency;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.ListItemAdapter;
import com.liner_exe.smartcart.databinding.FragmentListBinding;
import com.liner_exe.smartcart.modal.ListItemEditSheet;
import com.liner_exe.smartcart.utils.ThemeUtils;
import com.liner_exe.smartcart.viewmodel.ListItemsViewModel;
import com.liner_exe.smartcart.viewmodel.SettingsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentList extends Fragment {
    private FragmentListBinding binding;
    private ListItemAdapter adapter;
    private ListItemsViewModel listItemsViewModel;
    private SettingsViewModel settingsViewModel;
    private String listName;
    private int listId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ThemeUtils.setSystemBarsFromAttributes(
                requireActivity().getWindow(),
                com.google.android.material.R.attr.colorSurfaceContainer,
                com.google.android.material.R.attr.colorSurface
        );

        listItemsViewModel = new ViewModelProvider(requireActivity()).get(ListItemsViewModel.class);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        listItemsViewModel.setSortByCategory(false);

        handleArguments();

        setupToolbar();
        setupSortChips();
        setupRecyclerView();
        setupFab();
        setupTotalSum();

        observeViewModel();
    }

    private void handleArguments() {
        if (getArguments() != null) {
            FragmentListArgs args = FragmentListArgs.fromBundle(getArguments());

            listName = args.getListName();
            listId = args.getListId();

            listItemsViewModel.setCurrentListId(listId);
        }
    }

    private void setupToolbar() {
        binding.listAppBar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.listAppBar.setTitle(listName);
    }

    private void setupSortChips() {
        binding.chipGroupSort.setOnCheckedStateChangeListener(((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_sort_default) {
                listItemsViewModel.setSortByCategory(false);
            } else if (checkedId == R.id.chip_sort_category) {
                listItemsViewModel.setSortByCategory(true);
            }
        }));
    }

    private void setupRecyclerView() {
        adapter = new ListItemAdapter(new ListItemAdapter.OnListItemActionListener() {
            @Override
            public void onCheckbox(ListItem listItem) {
                listItemsViewModel.toggleItemStatus(listItem);
            }

            @Override
            public void onEdit(ListItem listItem) {
                ListItemEditSheet.newInstance(listItem)
                        .show(getChildFragmentManager(),
                        "ListItemEdit");
            }

            @Override
            public void onDelete(ListItem listItem) {
                listItemsViewModel.deleteListItemById(listItem.getId(), listId, listItem.getProduct().getId());
            }
        });

        binding.rvListItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListItems.setAdapter(adapter);

        adapter.setOnItemClickListener((listItem, position) -> {
            ListItemEditSheet.newInstance(listItem)
                    .show(getChildFragmentManager(),
                            "ListItemEdit");
        });
    }

    private void setupFab() {
        binding.fabAddListItem.setOnClickListener(v -> {
            NavDirections action = FragmentListDirections
                    .actionFragmentListToProductAddFragment(listId);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    private void setupTotalSum() {
        listItemsViewModel.totalSum.observe(getViewLifecycleOwner(), totalSum -> {
            Currency currency = settingsViewModel.currency.getValue();
            String totalText = currency != null ? currency.format(totalSum) : "";

            binding.textListTotalSum.setText(totalText);
        });
    }

    private void observeViewModel() {
        listItemsViewModel.listItems.observe(getViewLifecycleOwner(), newListItems -> {
            adapter.setItems(newListItems);
        });
    }
}