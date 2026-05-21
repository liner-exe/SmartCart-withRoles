package com.liner_exe.smartcart;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.liner_exe.smartcart.databinding.ActivityMainBinding;
import com.liner_exe.smartcart.utils.ThemeUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_nav_host);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            navController.addOnDestinationChangedListener((controller, destination, args) -> {
                if (destination.getId() == R.id.mainFragment) {
                    ThemeUtils.setSystemBarsFromAttributes(
                            getWindow(),
                            com.google.android.material.R.attr.colorSurfaceContainer,
                            com.google.android.material.R.attr.colorSurfaceContainer
                    );
                } else {
                    ThemeUtils.setSystemBarsFromAttributes(
                            getWindow(),
                            com.google.android.material.R.attr.colorSurfaceContainer,
                            com.google.android.material.R.attr.colorSurface
                    );
                }
            });
        }
    }
}