package com.liner_exe.smartcart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.ViewModelProvider;

import com.liner_exe.domain.models.Category;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.FragmentCategoryEditBinding;
import com.liner_exe.smartcart.utils.Utils;
import com.liner_exe.smartcart.viewmodel.CategoryViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryEditFragment extends Fragment {
    private FragmentCategoryEditBinding binding;
    private CategoryViewModel viewModel;
    private Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        handleArguments();

        setupToolbar();
        setupInitialState();
        setupFab();

        setupFabAnimation();
    }

    private void handleArguments() {
        if (getArguments() != null) {
            CategoryEditFragmentArgs args = CategoryEditFragmentArgs.fromBundle(getArguments());
            category = args.getCategory();
        }
    }

    private void setupToolbar() {
        binding.appToolbar.setTitle(category != null ? R.string.category_edit : R.string.category_add);
        binding.appToolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupInitialState() {
        if (category != null) {
            binding.ilCategoryEditName.setPrefixText(category.getEmoji() + " ");
            binding.etCategoryEditName.setText(category.getName());
            binding.etCategoryEditName.setSelection(category.getName().length());
        }

        binding.etCategoryEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateFields();
            }
        });
    }

    private void setupFab() {
        validateFields();

        binding.fabDoneCategory.setOnClickListener(v -> {
            if (category == null || (!category.getName().isEmpty() && !category.getEmoji().isEmpty())) {
                if (!binding.fabDoneCategory.isClickable()) return;

                saveCategory();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) return super.onCreateAnimation(transit, enter, nextAnim);

        Animation anim = AnimationUtils.loadAnimation(getContext(), nextAnim);
        if (enter) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.getRoot().post(() -> {
                        if (isAdded()) startEmojiPickerSmoothAppearance();
                    });
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        return anim;
    }

    private void setupFabAnimation() {
        WindowInsetsAnimationCompat.Callback callback = new WindowInsetsAnimationCompat.Callback(
                WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP
        ) {
            @Override
            public @NonNull WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                int keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
                int defaultMargin = Utils.convertDpToPx(16);

                if (keyboardHeight > defaultMargin) {
                    float translationY = keyboardHeight - defaultMargin;
                    binding.fabDoneCategory.setTranslationY(-translationY);
                } else {
                    binding.fabDoneCategory.setTranslationY(0);
                }

                return insets;
            }
        };

        ViewCompat.setWindowInsetsAnimationCallback(binding.fabDoneCategory, callback);
    }

    private void setupEmojiPicker() {
        binding.emojiPickerCategoryEdit.setOnEmojiPickedListener(item -> {
            String selectedEmoji = item.getEmoji();
            binding.ilCategoryEditName.setPrefixText(selectedEmoji + " ");

            validateFields();
        });
    }

    private void startEmojiPickerSmoothAppearance() {
        binding.emojiPickerCategoryEdit.setAlpha(0f);
        binding.emojiPickerCategoryEdit.setTranslationY(Utils.convertDpToPx(32));
        binding.emojiPickerCategoryEdit.setVisibility(View.VISIBLE);

        binding.emojiPickerCategoryEdit.post(() -> {
            binding.emojiPickerCategoryEdit.animate()
                    .alpha(1f)
                    .translationY(0)
                    .setDuration(400)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .withLayer()
                    .withEndAction(this::setupEmojiPicker)
                    .start();
        });
    }

    private void validateFields() {
        Editable nameInput = binding.etCategoryEditName.getText();
        String name = nameInput != null ? nameInput.toString().trim() : "";

        CharSequence prefix = binding.ilCategoryEditName.getPrefixText();
        String emoji = prefix != null ? prefix.toString().trim() : "";

        boolean isValid = !name.isEmpty() && !emoji.isEmpty();
        binding.fabDoneCategory.setClickable(isValid);
        binding.fabDoneCategory.setAlpha(isValid ? 1.0f : 0.5f);

        if (isValid) {
            binding.ilCategoryEditName.setError(null);
        }
    }

    private void saveCategory() {
        String name = binding.etCategoryEditName.getText().toString().trim();
        String emoji = binding.ilCategoryEditName.getPrefixText().toString().trim();

        if (name.isEmpty()) {
            binding.ilCategoryEditName.setError(getString(R.string.dialog_add_list_error_empty));
            return;
        }

        if (category == null) {
            viewModel.addCategory(new Category(name, emoji));
        } else {
            Category updatedCategory = new Category(category.getId(), name, emoji);
            viewModel.updateCategory(updatedCategory);
        }
    }
}