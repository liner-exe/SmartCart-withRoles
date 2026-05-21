package com.liner_exe.smartcart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.ShoppingList;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.ShoppingListsAdapter;
import com.liner_exe.smartcart.databinding.FragmentHomeBinding;
import com.liner_exe.smartcart.dialogs.ShoppingListDialogFragment;
import com.liner_exe.smartcart.viewmodel.ShoppingListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentHome extends Fragment {
    private FragmentHomeBinding binding;
    private ShoppingListsAdapter adapter;
    private ShoppingListViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);

        setupSearchLogic();
        setupRecyclerView();
        observeViewModel();
        bindDialog();
    }

    private void setupRecyclerView() {
        adapter = new ShoppingListsAdapter(
            new ShoppingListsAdapter.OnShoppingListActionListener() {
                @Override
                public void onRename(ShoppingList shoppingList) {
                    ShoppingListDialogFragment.newInstance(shoppingList.getName(), newName -> {
                        ShoppingList updatedShoppingList = new ShoppingList(
                                shoppingList.getId(), newName,
                                shoppingList.getTotalItems(), shoppingList.getBoughtItems(),
                                shoppingList.getCreatedAt()
                        );
                        viewModel.updateList(updatedShoppingList);
                    }).show(getChildFragmentManager(), "RenameListDialog");
                }

                @Override
                public void onDelete(ShoppingList shoppingList) {
                    viewModel.deleteListById(shoppingList.getId());
                }
            }
        );

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((item, position) -> {
            if (item instanceof ShoppingList) {
                ShoppingList shoppingList = (ShoppingList) item;

                NavDirections action = MainFragmentDirections
                        .actionMainFragmentToFragmentList()
                        .setListId(shoppingList.getId())
                        .setListName(shoppingList.getName());

                NavHostFragment.findNavController(requireParentFragment().requireParentFragment()).navigate(action);
            }
        });
    }

    private void observeViewModel() {
        viewModel.isDbEmpty.observe(getViewLifecycleOwner(), isEmpty -> {
            binding.searchInputLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        viewModel.uiStateLists.observe(getViewLifecycleOwner(), newLists -> {
            adapter.setItems(newLists);

            boolean isEmpty = newLists == null || newLists.isEmpty();
            boolean isQueryEmpty = binding.searchEditText.getText().toString().trim().isEmpty();

            updateVisibilityUI(isEmpty, !isQueryEmpty);
        });
    }

    private void bindDialog() {
        binding.fab.setOnClickListener(v -> {
            ShoppingListDialogFragment.newInstance(null, name -> {
                viewModel.addList(new ShoppingList(name));
            }).show(getChildFragmentManager(), "AddListDialog");
        });
    }

    private void setupSearchLogic() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                viewModel.setSearchQuery(s.toString());
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
                binding.emptyStateTitle.setText(R.string.home_empty_list_title);
                binding.emptyStateMessage.setText(R.string.home_empty_list_message);
            }
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyStateView.setVisibility(View.GONE);
            updateScrollFlags(false);
        }
    }

    private void updateScrollFlags(boolean isEmpty) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.searchInputLayout.getLayoutParams();
        if (isEmpty) {
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        binding.searchInputLayout.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.setSearchQuery("");
    }
}