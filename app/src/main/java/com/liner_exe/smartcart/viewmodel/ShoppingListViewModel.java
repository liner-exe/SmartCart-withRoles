package com.liner_exe.smartcart.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.liner_exe.domain.models.DisplayItem;
import com.liner_exe.domain.models.ShoppingList;
import com.liner_exe.domain.repository.IShoppingListRepository;
import com.liner_exe.domain.usecases.GetGroupedShoppingListsUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ShoppingListViewModel extends BaseViewModel {
    private final IShoppingListRepository repository;
    private final GetGroupedShoppingListsUseCase getGroupedShoppingListsUseCase;

    private final MutableLiveData<List<ShoppingList>> _allShoppingLists = new MutableLiveData<>();

    private final MediatorLiveData<List<DisplayItem>> _uiStateLists = new MediatorLiveData<>();
    public LiveData<List<DisplayItem>> uiStateLists = _uiStateLists;

    private final MutableLiveData<String> _searchQuery = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> _isDbEmpty = new MutableLiveData<>(true);
    public LiveData<Boolean> isDbEmpty = _isDbEmpty;


    @Inject
    public ShoppingListViewModel(
            IShoppingListRepository repository,
            GetGroupedShoppingListsUseCase getGroupedShoppingListsUseCase
    ) {
        this.repository = repository;
        this.getGroupedShoppingListsUseCase = getGroupedShoppingListsUseCase;

        _uiStateLists.addSource(_allShoppingLists, stores -> combineAndProcess());
        _uiStateLists.addSource(_searchQuery, query -> combineAndProcess());

        subscribeToLists();
    }

    public void setSearchQuery(String query) {
        _searchQuery.setValue(query);
    }

    private void combineAndProcess() {
        List<ShoppingList> list = _allShoppingLists.getValue();
        String query = _searchQuery.getValue();

        List<DisplayItem> result = getGroupedShoppingListsUseCase.execute(list, query);
        _uiStateLists.setValue(result);
    }

    public void addList(ShoppingList shoppingList) {
        executeCompletable(repository.add(shoppingList), "addList");
    }

    public void updateList(ShoppingList shoppingList) {
        executeCompletable(repository.update(shoppingList), "updateList");
    }

    public void deleteListById(int id) {
        executeCompletable(repository.deleteById(id), "deleteListById");
    }

    private void subscribeToLists() {
        disposable.add(repository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shoppingLists -> {
                            _allShoppingLists.setValue(shoppingLists);
                            _isDbEmpty.setValue(shoppingLists == null || shoppingLists.isEmpty());
                        },
                        throwable -> {
                            Log.e("SC_DB_ERROR", "error vm: " + throwable.getMessage());
                        }
                ));
    }
}