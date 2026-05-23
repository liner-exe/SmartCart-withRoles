package com.liner_exe.smartcart_roles.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.google.android.material.R.attr;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.liner_exe.domain.enums.Currency;
import com.liner_exe.domain.enums.UserRole;
import com.liner_exe.domain.models.monitoring.CategoryProgress;
import com.liner_exe.domain.models.monitoring.StoreProgress;
import com.liner_exe.smartcart_roles.R;
import com.liner_exe.smartcart_roles.databinding.FragmentMonitoringBinding;
import com.liner_exe.smartcart_roles.utils.PremiumDialogManager;
import com.liner_exe.smartcart_roles.utils.ThemeUtils;
import com.liner_exe.smartcart_roles.viewmodel.MonitoringViewModel;
import com.liner_exe.smartcart_roles.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentMonitoring extends Fragment {
    private FragmentMonitoringBinding binding;
    private MonitoringViewModel monitoringViewModel;
    private SettingsViewModel settingsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMonitoringBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monitoringViewModel = new ViewModelProvider(this).get(MonitoringViewModel.class);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        setupPieChart();
        setupStoresPieChart();

        binding.btnUnlockPremium.setOnClickListener(v -> PremiumDialogManager.showPremiumInfoDialog(requireContext(), settingsViewModel));

        observeViewModel();
    }

    private void observeViewModel() {
        settingsViewModel.userRole.observe(getViewLifecycleOwner(), role -> {
            if (role == UserRole.PREMIUM) {
                binding.premiumStub.setVisibility(View.GONE);
                binding.containerMonitoringContent.setVisibility(View.VISIBLE);

                if (monitoringViewModel.expenses.getValue() != null) {
                    updateCategoriesChart(monitoringViewModel.expenses.getValue());
                }
                if (monitoringViewModel.storeExpenses.getValue() != null) {
                    updateStoresChart(monitoringViewModel.storeExpenses.getValue());
                }
                if (monitoringViewModel.totalSum.getValue() != null) {
                    updateTotalSumText(monitoringViewModel.totalSum.getValue());
                }
            } else {
                binding.premiumStub.setVisibility(View.VISIBLE);
                binding.containerMonitoringContent.setVisibility(View.GONE);
            }
        });

        monitoringViewModel.totalSum.observe(getViewLifecycleOwner(), this::updateTotalSumText);
        monitoringViewModel.expenses.observe(getViewLifecycleOwner(), this::updateCategoriesChart);
        monitoringViewModel.storeExpenses.observe(getViewLifecycleOwner(), this::updateStoresChart);
    }

    private void updateTotalSumText(Double total) {
        if (settingsViewModel.userRole.getValue() != UserRole.PREMIUM) return;

        Currency currency = settingsViewModel.currency.getValue();
        String totalText = currency != null ? currency.format(total) : "";
        binding.textTotalSum.setText(totalText);
    }

    private void updateCategoriesChart(List<CategoryProgress> progressList) {
        if (settingsViewModel.userRole.getValue() != UserRole.PREMIUM) return;
        if (progressList == null || progressList.isEmpty()) {
            binding.pieChart.clear();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for (CategoryProgress progress : progressList) {
            if (progress.getTotalExpense() > 0) {
                float value = (float) progress.getTotalExpense();
                String label = progress.getEmoji() + " " + progress.getName();
                entries.add(new PieEntry(value, label));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.MATERIAL_COLORS) colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(binding.pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        binding.pieChart.setData(data);
        binding.pieChart.highlightValues(null);
        binding.pieChart.invalidate();
    }

    private void updateStoresChart(List<StoreProgress> storeList) {
        if (settingsViewModel.userRole.getValue() != UserRole.PREMIUM) return;
        if (storeList == null || storeList.isEmpty()) {
            binding.pieChartStores.clear();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for (StoreProgress progress : storeList) {
            if (progress.getTotalExpense() > 0) {
                entries.add(new PieEntry((float) progress.getTotalExpense(), progress.getStoreName()));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS) colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS) colors.add(c);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(binding.pieChartStores));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        binding.pieChartStores.setData(data);
        binding.pieChartStores.highlightValues(null);
        binding.pieChartStores.invalidate();
    }

    private void setupPieChart() {
        int textColor = ThemeUtils.getThemeColor(requireContext(), attr.colorOnSurface);

        PieChart pieChart = binding.pieChart;
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setNoDataText(getString(R.string.monitoring_pie_error_message));
        pieChart.setNoDataTextColor(textColor);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setCenterText(getString(R.string.monitoring_pie_title));
        pieChart.setCenterTextColor(textColor);
        pieChart.setCenterTextSize(16f);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setTextColor(textColor);
        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }

    private void setupStoresPieChart() {
        int textColor = ThemeUtils.getThemeColor(requireContext(), attr.colorOnSurface);

        PieChart pieChart = binding.pieChartStores;
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setNoDataText(getString(R.string.monitoring_pie_error_message));
        pieChart.setNoDataTextColor(textColor);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setCenterText(getString(R.string.monitoring_pie_stores_title));
        pieChart.setCenterTextColor(textColor);
        pieChart.setCenterTextSize(16f);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setTextColor(textColor);
        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }
}