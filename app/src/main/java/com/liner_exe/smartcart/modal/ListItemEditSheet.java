package com.liner_exe.smartcart.modal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.liner_exe.data.storage.SettingsManager;
import com.liner_exe.domain.enums.Currency;
import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.ListItem;
import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.models.Store;
import com.liner_exe.domain.utils.QuantityCalculator;
import com.liner_exe.domain.utils.formatters.QuantityFormatter;
import com.liner_exe.domain.utils.validators.PriceValidator;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.BottomSheetEditListItemBinding;
import com.liner_exe.smartcart.fragments.FragmentListDirections;
import com.liner_exe.smartcart.viewmodel.CategoryViewModel;
import com.liner_exe.smartcart.viewmodel.ListItemsViewModel;
import com.liner_exe.smartcart.viewmodel.ProductViewModel;
import com.liner_exe.smartcart.viewmodel.StoresViewModel;

public class ListItemEditSheet extends BottomSheetDialogFragment {
    private BottomSheetEditListItemBinding binding;
    private ListItemsViewModel listItemsViewModel;
    private CategoryViewModel categoryViewModel;
    private ProductViewModel productViewModel;
    private StoresViewModel storesViewModel;
    private ListItem listItem;

    private SettingsManager settingsManager;
    private Currency currentCurrency;
    private boolean decimalMode = false;

    public static ListItemEditSheet newInstance(ListItem listItem) {
        ListItemEditSheet sheet = new ListItemEditSheet();
        Bundle args = new Bundle();
        args.putSerializable("arg_listItem", listItem);
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleArguments();

        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        listItemsViewModel = new ViewModelProvider(requireActivity()).get(ListItemsViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        storesViewModel = new ViewModelProvider(requireActivity()).get(StoresViewModel.class);

        categoryViewModel.resetSelectedCategory();
        storesViewModel.resetSelectedStore();

        if (listItem.getProduct().getCategoryId() != null && categoryViewModel.selectedCategory.getValue() == null) {
            categoryViewModel.loadCategoryById(listItem.getProduct().getCategoryId());
        }

        if (listItem.getStoreId() != null && storesViewModel.selectedStore.getValue() == null) {
            storesViewModel.loadStoreById(listItem.getStoreId());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetEditListItemBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.liEditNameEditText.setText(listItem.getProduct().getName());
        binding.liEditQuantityEditText.setText(QuantityFormatter.format(listItem.getQuantity()));
        binding.liEditPriceEditText.setText(PriceValidator.format(listItem.getPrice()));
        binding.liEditMeasureUnitEditText.setText(listItem.getUnit());

        binding.liEditPriceInputLayout.setHint(getString(R.string.list_item_edit_price_per_unit,
                listItem.getUnit()));

        categoryViewModel.selectedCategory.observe(getViewLifecycleOwner(), category -> {
            if (category != null) {
                binding.liEditCategoryName.setText(category.getEmoji() + " " + category.getName());
            } else {
                binding.liEditCategoryName.setText(getString(R.string.list_item_no_category));
            }
        });

        storesViewModel.selectedStore.observe(getViewLifecycleOwner(), store -> {
            if (store != null) {
                binding.liEditStoreName.setText(store.getName());
            } else {
                binding.liEditStoreName.setText(getString(R.string.list_item_no_store));
            }
        });

        binding.liEditCardCategory.setOnClickListener(v -> {
            NavDirections action = FragmentListDirections
                    .actionFragmentListToCategorySelectionFragment(listItem.getProduct());
            NavHostFragment.findNavController(this).navigate(action);
        });

        binding.liEditCardStore.setOnClickListener(v -> {
            NavDirections action = FragmentListDirections
                    .actionFragmentListToStoreSelectionFragment(listItem);
            NavHostFragment.findNavController(this).navigate(action);
        });

        updateTotalDisplay();

        binding.liEditQuantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String text = binding.liEditQuantityEditText.getText().toString();

                decimalMode = text.contains(".") || text.contains(",");
            }
        });

        binding.liEditButtonMinus.setOnClickListener(v -> {
            double current = QuantityFormatter.parse(binding.liEditQuantityEditText.getText().toString());
            updateQuantityField(QuantityCalculator.decrement(current, decimalMode));
        });

        binding.liEditButtonPlus.setOnClickListener(v -> {
            double current = QuantityFormatter.parse(binding.liEditQuantityEditText.getText().toString());
            updateQuantityField(QuantityCalculator.increment(current, decimalMode));
        });

        binding.liEditButtonDelete.setOnClickListener(v -> {
            listItemsViewModel.deleteListItemById(listItem.getId(), listItem.getListId(),
                    listItem.getProduct().getId());
            dismiss();
        });

        binding.liEditPriceInputLayout.setError(null);

        binding.liEditButtonDone.setOnClickListener(v -> {
            if (!updateListItemFromUI()) return;

            Product updatedProduct = new Product(
                    listItem.getProduct().getId(),
                    listItem.getProduct().getName(),
                    listItem.getProduct().getCategoryId()
            );

            productViewModel.updateProduct(updatedProduct);
            listItemsViewModel.updateListItem(listItem);

            dismiss();
        });

        bindTextWatchers();
    }

    private void handleArguments() {
        if (getArguments() != null) {
            listItem = (ListItem) getArguments().getSerializable("arg_listItem");
        }

        settingsManager = new SettingsManager(requireContext());
        currentCurrency = settingsManager.getCurrency();
    }

    private void updateTotalDisplay() {
        String quantityString = binding.liEditQuantityEditText.getText().toString();
        String priceString = binding.liEditPriceEditText.getText().toString();

        double quantity = QuantityFormatter.parse(quantityString);
        double price = (priceString.isEmpty() || !PriceValidator.isValid(priceString)) ?
                0 : PriceValidator.parse(priceString);

        binding.liEditTotalPriceText.setText(currentCurrency.format(price * quantity));
    }

    private void updateQuantityField(double newValue) {
        String formatted = QuantityFormatter.format(newValue);
        binding.liEditQuantityEditText.setText(formatted);
        binding.liEditQuantityEditText.setSelection(formatted.length());
    }

    private boolean updateListItemFromUI() {
        String currentName = binding.liEditNameEditText.getText().toString().trim();
        String quantityText = binding.liEditQuantityEditText.getText().toString();
        String priceText = binding.liEditPriceEditText.getText().toString();
        String unit = binding.liEditMeasureUnitEditText.getText().toString().trim();

        double quantity = quantityText.isEmpty() ? 1 : Double.parseDouble(quantityText);
        double price = PriceValidator.parse(priceText);

        Store currentStore = storesViewModel.selectedStore.getValue();
        Integer storeId = (currentStore != null) ? currentStore.getId() : null;

        Category currentCategory = categoryViewModel.selectedCategory.getValue();
        Integer categoryId = (currentCategory != null) ? currentCategory.getId() : null;

        listItem = new ListItem(
                listItem.getId(),
                new Product(
                        listItem.getProduct().getId(),
                        currentName,
                        categoryId
                ),
                quantity,
                price,
                unit,
                listItem.isBought(),
                listItem.getListId(),
                storeId
        );

        return true;
    }

    private void bindTextWatchers() {
        binding.liEditMeasureUnitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                binding.liEditPriceInputLayout.setHint(
                        getString(R.string.list_item_edit_price_per_unit, s)
                );
            }
        });

        TextWatcher totalPriceUpdateTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String priceInput = binding.liEditPriceEditText.getText().toString();

                if (!priceInput.isEmpty() && !PriceValidator.isValid(priceInput)) {
                    binding.liEditPriceInputLayout.setError(
                            getString(R.string.list_item_edit_default_price_format_error)
                    );
                } else {
                    binding.liEditPriceInputLayout.setError(null);
                }

                updateTotalDisplay();
            }
        };

        binding.liEditQuantityEditText.addTextChangedListener(totalPriceUpdateTextWatcher);
        binding.liEditPriceEditText.addTextChangedListener(totalPriceUpdateTextWatcher);
    }
}