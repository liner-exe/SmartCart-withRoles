package com.liner_exe.smartcart.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liner_exe.domain.models.ListItem;
import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.repository.IListItemRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@HiltViewModel
public class ListItemsViewModel extends BaseViewModel {
    private final IListItemRepository repository;

    private final MutableLiveData<List<ListItem>> _listItems = new MutableLiveData<>();
    public LiveData<List<ListItem>> listItems = _listItems;

    private final BehaviorSubject<Integer> _currentListId = BehaviorSubject.create();
    private final BehaviorSubject<Boolean> _sortByCategory = BehaviorSubject.createDefault(false);

    private final MutableLiveData<Double> _totalSum = new MutableLiveData<>();
    public LiveData<Double> totalSum = _totalSum;

    @Inject
    public ListItemsViewModel(IListItemRepository repository) {
        this.repository = repository;
        observeCurrentList();
        observeTotalSum();
    }

    public void addProductAsListItem(Product product, String defaultUnit) {
        Integer currentId = _currentListId.getValue();

        if (currentId != null) {
            ListItem newItem = new ListItem(currentId, product, defaultUnit);
            executeCompletable(repository.add(newItem), "addListItem");
        }
    }

    public void setCurrentListId(int listId) {
        _currentListId.onNext(listId);
    }

    public void setSortByCategory(boolean sortByCategory) {
        _sortByCategory.onNext(sortByCategory);
    }

    public void deleteListItemById(int itemId, int listId, int productId) {
        executeCompletable(repository.deleteById(itemId, listId, productId),
                "deleteListItemById");
    }

    public void updateListItem(ListItem listItem) {
        executeCompletable(repository.update(listItem), "updateListItem");
    }

    public void toggleItemStatus(ListItem listItem) {
        boolean newStatus = !listItem.isBought();
        executeCompletable(repository.updateItemStatus(
                listItem.getId(),
                listItem.getListId(),
                listItem.getProduct().getId(),
                newStatus), "toggleItemStatus");
    }

    private void observeCurrentList() {
        disposable.add(Observable.combineLatest(
                _currentListId.distinctUntilChanged(),
                _sortByCategory,
                        (listId, sortByCategory) -> new Object[] {listId, sortByCategory}
            )
            .switchMap(params -> {
                int listId = (int) params[0];
                boolean sortByCategory = (boolean) params[1];
                return repository.getItemsForList(listId, sortByCategory).toObservable();
            })
            .subscribe(
                    _listItems::postValue,
                    throwable -> { Log.e("SC_DB_ERROR", "error vm: " + throwable.getMessage()); }
            )
        );
    }

    private void observeTotalSum() {
        disposable.add(_currentListId
            .distinctUntilChanged()
            .switchMap(listId -> repository.getListTotalSum(listId).toObservable())
            .subscribe(
                    sum -> _totalSum.postValue(sum != null ? sum : 0.0),
                    throwable -> {
                        Log.e("SC_DB_ERROR", "error vm total sum: " + throwable.getMessage());
                    })
        );
    }
}