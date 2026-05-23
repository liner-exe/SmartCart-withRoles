package com.liner_exe.smartcart_roles.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liner_exe.domain.enums.UserRole;
import com.liner_exe.smartcart_roles.R;
import com.liner_exe.smartcart_roles.databinding.FragmentMainBinding;
import com.liner_exe.smartcart_roles.viewmodel.SettingsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    FragmentMainBinding binding;
    private SettingsViewModel settingsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.tabs_nav_host);

        setupNavigation();
        setupPremiumObserver();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.tabs_nav_host);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        }
    }

    private void setupPremiumObserver() {
        settingsViewModel.userRole.observe(getViewLifecycleOwner(), role -> {
            if (role == UserRole.PREMIUM) {
                binding.premiumBadge.setVisibility(View.VISIBLE);
            } else {
                binding.premiumBadge.setVisibility(View.GONE);
            }
        });
    }
}