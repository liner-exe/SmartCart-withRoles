package com.liner_exe.data.repository;

import com.liner_exe.data.local.dao.CategoryDao;
import com.liner_exe.data.local.entities.CategoryEntity;
import com.liner_exe.data.mapper.CategoryMapper;
import com.liner_exe.domain.models.Category;
import com.liner_exe.domain.repository.ICategoryRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryRepositoryImpl implements ICategoryRepository {
    private final CategoryDao dao;

    @Inject
    public CategoryRepositoryImpl(CategoryDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable add(Category category) {
        return dao.insert(CategoryMapper.toEntity(category));
    }

    @Override
    public Flowable<List<Category>> getAll() {
        return dao.getAll().map(CategoryMapper::toModelList);
    }

    @Override
    public Single<Category> findById(int id) {
        return Single.fromCallable(() -> {
            CategoryEntity category = dao.findById(id);
            return CategoryMapper.toModel(category);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable update(Category category) {
        return dao.update(CategoryMapper.toEntity(category));
    }

    @Override
    public Completable deleteById(int id) {
        return dao.deleteById(id);
    }
}
