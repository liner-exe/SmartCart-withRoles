package com.liner_exe.smartcart.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.DialogAddListBinding;

public class ShoppingListDialogFragment extends DialogFragment {
    public interface OnListAddedListener {
        void onAdd(String name);
    }

    private OnListAddedListener listener;
    private String currentName;

    private DialogAddListBinding binding;

    public static ShoppingListDialogFragment newInstance(String currentName, OnListAddedListener listener) {
        ShoppingListDialogFragment fragment = new ShoppingListDialogFragment();
        fragment.listener = listener;
        fragment.currentName = currentName;

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        binding = DialogAddListBinding.inflate(getLayoutInflater());
        TextInputEditText editText = binding.editListName;

        if (currentName != null) {
            editText.setText(currentName);
            editText.setSelection(currentName.length());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(currentName == null ? R.string.dialog_add_list_title : R.string.action_rename)
                .setMessage(R.string.dialog_add_list_message)
                .setView(binding.getRoot())
                .setPositiveButton(currentName == null ? R.string.action_add : R.string.action_rename,
                        null)
                .setNegativeButton(R.string.action_cancel, null)
                .create();

        setupListeners(dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            editText.requestFocus();
        }

        return dialog;
    }

    private void setupListeners(AlertDialog dialog) {
        binding.editListName.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptSave(binding.editListName, binding.listAddInputLayout, dialog);

                return true;
            }

            return false;
        });

        binding.editListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.listAddInputLayout.setError(null);
                binding.listAddInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (button != null) {
                button.setOnClickListener(v -> {
                    attemptSave(binding.editListName, binding.listAddInputLayout, dialog);
                });
            }
        });
    }

    private void attemptSave(TextInputEditText editText, TextInputLayout inputLayout,
                             AlertDialog dialog) {
        String name = editText.getText() != null ? editText.getText().toString().trim() : "";

        if (name.isEmpty()) {
            inputLayout.setError(getString(R.string.dialog_add_list_error_empty));
            inputLayout.setErrorEnabled(true);
        } else {
            if (listener != null) {
                listener.onAdd(name);
            }
            dialog.dismiss();
        }
    }
}
