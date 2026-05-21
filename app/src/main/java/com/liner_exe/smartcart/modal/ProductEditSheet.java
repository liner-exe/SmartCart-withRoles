package com.liner_exe.smartcart.modal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.Product;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.BottomSheetEditProductBinding;
import com.liner_exe.smartcart.databinding.ItemCategoryBinding;
import com.liner_exe.smartcart.fragments.ProductsManagementFragmentDirections;
import com.liner_exe.smartcart.viewmodel.CategoryViewModel;
import com.liner_exe.smartcart.viewmodel.ProductViewModel;

public class ProductEditSheet extends BottomSheetDialogFragment {
    private BottomSheetEditProductBinding binding;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private Product product;

    public static ProductEditSheet newInstance(Product product) {
        ProductEditSheet sheet = new ProductEditSheet();
        Bundle args = new Bundle();
        args.putSerializable("arg_product", product);
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("arg_product");
        }

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        categoryViewModel.resetSelectedCategory();

        if (product.getCategoryId() != null && categoryViewModel.selectedCategory.getValue() == null) {
            categoryViewModel.loadCategoryById(product.getCategoryId());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_edit_product, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setProduct(product);

        binding.productEditButtonDelete.setOnClickListener(v -> {
            productViewModel.deleteProductById(product.getId());
            dismiss();
        });

        binding.productEditButtonDone.setOnClickListener(v -> {
            updateProductFromUI();
            productViewModel.updateProduct(product);
            dismiss();
        });

        binding.productEditButtonChangeCategory.setOnClickListener(v -> {
            NavDirections action = ProductsManagementFragmentDirections
                    .actionProductsManagementFragmentToCategorySelectionFragment(product);
            NavHostFragment.findNavController(this).navigate(action);
        });

        ItemCategoryBinding productEditCategoryDisplay = binding.productEditCategoryDisplay;

        productEditCategoryDisplay.categoryItemButtonMore.setVisibility(View.GONE);

        categoryViewModel.selectedCategory.observe(getViewLifecycleOwner(), category -> {
            if (category != null) {
                productEditCategoryDisplay.categoryItemTitle.setText(category.getName());
                productEditCategoryDisplay.categoryItemEmoji.setText(category.getEmoji());
                binding.executePendingBindings();
                binding.getRoot().requestLayout();
            } else {
                productEditCategoryDisplay.categoryItemTitle.setText(R.string.no_category);
                productEditCategoryDisplay.categoryItemEmoji.setText("");
            }
        });
    }

    private void updateProductFromUI() {
        if (binding.productEditNameEditText.getText() == null) return;

        Category currentCategory = categoryViewModel.selectedCategory.getValue();
        Integer categoryId = (currentCategory != null) ? currentCategory.getId() : null;

        String currentName = binding.productEditNameEditText.getText().toString().trim();
        product = new Product(product.getId(), currentName, categoryId);
    }
}
