package com.liner_exe.smartcart.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.models.Store;
import com.liner_exe.domain.repository.IProductRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ProductViewModel extends BaseViewModel {
    private final IProductRepository repository;

    private final MutableLiveData<List<Product>> _allProducts = new MutableLiveData<>();

    private final MediatorLiveData<List<Product>> _filteredProducts = new MediatorLiveData<>();
    public LiveData<List<Product>> filteredProducts = _filteredProducts;

    private final MutableLiveData<String> _searchQuery = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> _isDbEmpty = new MutableLiveData<>(true);
    public LiveData<Boolean> isDbEmpty = _isDbEmpty;

    @Inject
    public ProductViewModel(IProductRepository repository) {
        this.repository = repository;

        _filteredProducts.addSource(_allProducts, stores -> performFilter());
        _filteredProducts.addSource(_searchQuery, query -> performFilter());

        subscribeToProducts();
    }

    public void setSearchQuery(String query) {
        _searchQuery.setValue(query);
    }

    public void performFilter() {
        List<Product> list = _allProducts.getValue();
        String query = _searchQuery.getValue();

        if (list == null) {
            _filteredProducts.setValue(new ArrayList<>());
            return;
        }

        if (query == null || query.isEmpty()) {
            _filteredProducts.setValue(list);
        } else {
            List<Product> filtered = new ArrayList<>();
            String pattern = query.toLowerCase().trim();
            for (Product product : list) {
                if (product.getName().toLowerCase().contains(pattern)) {
                    filtered.add(product);
                }
            }
            _filteredProducts.setValue(filtered);
        }
    }

    public void addProduct(Product product) {
        executeCompletable(repository.add(product), "addProduct");
    }

    public void updateProduct(Product product) {
        executeCompletable(repository.update(product), "updateProduct");
    }

    public void deleteProductById(int id) {
        executeCompletable(repository.deleteById(id), "deleteProductById");
    }

    private void subscribeToProducts() {
        disposable.add(repository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                            _allProducts.setValue(products);
                            _isDbEmpty.setValue(products == null || products.isEmpty());
                        },
                        throwable -> {
                            Log.e("SC_DB_ERROR", "error vm: " + throwable.getMessage());
                        }
                ));
    }
}
