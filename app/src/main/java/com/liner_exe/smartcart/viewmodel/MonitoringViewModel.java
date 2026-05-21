package com.liner_exe.smartcart.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liner_exe.domain.models.monitoring.CategoryProgress;
import com.liner_exe.domain.models.monitoring.StoreProgress;
import com.liner_exe.domain.repository.IListItemRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@HiltViewModel
public class MonitoringViewModel extends BaseViewModel {
    private final IListItemRepository repository;

    private final MutableLiveData<List<CategoryProgress>> _expenses = new MutableLiveData<>();
    public LiveData<List<CategoryProgress>> expenses = _expenses;

    private final MutableLiveData<Double> _totalSum = new MutableLiveData<>(0.0);
    public LiveData<Double> totalSum = _totalSum;

    private final MutableLiveData<List<StoreProgress>> _storeExpenses = new MutableLiveData<>();
    public LiveData<List<StoreProgress>> storeExpenses = _storeExpenses;

    private final BehaviorSubject<Boolean> _isDayMode = BehaviorSubject.createDefault(true);

    @Inject
    public MonitoringViewModel(IListItemRepository repository) {
        this.repository = repository;
        observeExpenses();
        observeStoreExpenses();
    }

    private void observeExpenses() {
        disposable.add(repository.getExpensesGroupedByCategory()
                .subscribe(
                        list -> {
                            _expenses.postValue(list);

                            double sum = 0;
                            for (CategoryProgress item : list) {
                                sum += item.getTotalExpense();
                            }
                            _totalSum.postValue(sum);
                        },
                        throwable -> {
                            Log.e("SC_DB_ERROR", "Monitoring error: " + throwable.getMessage());
                        }
                ));
    }

    private void observeStoreExpenses() {
        disposable.add(repository.getExpensesGroupedByStore()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        _storeExpenses::postValue,
                        throwable -> Log.e("SC_DB_ERROR", "Store monitoring error: " + throwable.getMessage())
                ));
    }

    public void setPeriodMode(boolean isDayMode) {
        _isDayMode.onNext(isDayMode);
    }
}
