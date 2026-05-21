package com.liner_exe.smartcart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.Product;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.adapters.ProductsManagementAdapter;
import com.liner_exe.smartcart.databinding.FragmentProductsManagementBinding;
import com.liner_exe.smartcart.dialogs.ProductDialogFragment;
import com.liner_exe.smartcart.modal.ProductEditSheet;
import com.liner_exe.smartcart.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductsManagementFragment extends Fragment {
    private FragmentProductsManagementBinding binding;
    private ProductsManagementAdapter adapter;
    private ProductViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductsManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

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
        adapter = new ProductsManagementAdapter(
            new ProductsManagementAdapter.OnProductActionListener() {
                @Override
                public void onEdit(Product product) {
                    ProductEditSheet.newInstance(product).show(
                            getChildFragmentManager(), "ProductEditSheet"
                    );
                }

                @Override
                public void onRename(Product product) {
                    ProductDialogFragment.newInstance(product.getName(), newName -> {
                        Product updatedProduct = new Product(product.getId(), newName, product.getCategoryId());
                        viewModel.updateProduct(updatedProduct);
                    }).show(getChildFragmentManager(), "RenameProductDialog");
                }

                @Override
                public void onDelete(Product product) {
                    viewModel.deleteProductById(product.getId());
                }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(((product, position) -> {
            ProductEditSheet.newInstance(product).show(
                    getChildFragmentManager(), "ProductEditSheet"
            );
        }));
    }

    private void observeViewModel() {
        viewModel.isDbEmpty.observe(getViewLifecycleOwner(), isEmpty -> {
            binding.searchInputLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        viewModel.filteredProducts.observe(getViewLifecycleOwner(), newProducts -> {
            adapter.setItems(newProducts);

            boolean isQueryEmpty = binding.searchEditText.getText().toString().trim().isEmpty();
            updateVisibilityUI(newProducts == null || newProducts.isEmpty(), !isQueryEmpty);
        });
    }

    private void bindDialog() {
        binding.fab.setOnClickListener(v -> {
            ProductDialogFragment.newInstance(null, name -> {
                viewModel.addProduct(new Product(name));
            }).show(getChildFragmentManager(), "AddProductDialog");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.setSearchQuery("");
    }
}