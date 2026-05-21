package com.liner_exe.smartcart.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.models.Store;
import com.liner_exe.domain.repository.ICategoryRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class CategoryViewModel extends BaseViewModel {
    private final ICategoryRepository repository;

    private final MutableLiveData<List<Category>> _allCategories = new MutableLiveData<>();

    private final MediatorLiveData<List<Category>> _filteredCategories = new MediatorLiveData<>();
    public LiveData<List<Category>> filteredCategories = _filteredCategories;

    private final MutableLiveData<String> _searchQuery = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> _isDbEmpty = new MutableLiveData<>(true);
    public LiveData<Boolean> isDbEmpty = _isDbEmpty;

    private final MutableLiveData<Category> _selectedCategory = new MutableLiveData<>();
    public LiveData<Category> selectedCategory = _selectedCategory;

    @Inject
    public CategoryViewModel(ICategoryRepository repository) {
        this.repository = repository;

        _filteredCategories.addSource(_allCategories, categories -> performFilter());
        _filteredCategories.addSource(_searchQuery, query -> performFilter());

        subscribeToCategories();
    }

    public void setSearchQuery(String query) {
        if (query.equals(_searchQuery.getValue())) return;
        _searchQuery.setValue(query);
    }

    public void performFilter() {
        List<Category> list = _allCategories.getValue();
        String query = _searchQuery.getValue();

        if (list == null) {
            _filteredCategories.setValue(new ArrayList<>());
            return;
        }

        if (query == null || query.isEmpty()) {
            _filteredCategories.setValue(list);
        } else {
            List<Category> filtered = new ArrayList<>();
            String pattern = query.toLowerCase().trim();
            for (Category category : list) {
                String categoryName = category.getEmoji() + category.getName().toLowerCase();
                if (categoryName.contains(pattern)) {
                    filtered.add(category);
                }
            }
            _filteredCategories.setValue(filtered);
        }
    }

    public void addCategory(Category category) {
        executeCompletable(repository.add(category), "addCategory");
    }

    public void loadCategoryById(int id) {
        disposable.add(repository.findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        _selectedCategory::setValue,
                        throwable -> {
                            Log.e("DB_ERROR", "error vm: " + throwable.getMessage());
                        }
                ));
    }

    public void updateCategory(Category category) {
        executeCompletable(repository.update(category), "updateCategory");
    }

    public void deleteCategoryById(int id) {
        executeCompletable(repository.deleteById(id), "deleteCategoryById");
    }

    public void resetSelectedCategory() {
        _selectedCategory.setValue(null);
    }

    public void setSelectedCategory(Category category) {
        _selectedCategory.postValue(category);
    }

    private void subscribeToCategories() {
        disposable.add(repository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                            _allCategories.setValue(categories);
                            _isDbEmpty.setValue(categories == null || categories.isEmpty());
                        },
                        throwable -> {
                            Log.e("SC_DB_ERROR", "error vm: " + throwable.getMessage());
                        }
                ));
    }
}
