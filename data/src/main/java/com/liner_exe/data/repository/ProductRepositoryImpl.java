package com.liner_exe.data.repository;

import com.liner_exe.data.local.dao.ProductDao;
import com.liner_exe.data.local.entities.ProductEntity;
import com.liner_exe.data.mapper.ProductMapper;
import com.liner_exe.domain.models.Product;
import com.liner_exe.domain.repository.IProductRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ProductRepositoryImpl implements IProductRepository {
    private final ProductDao dao;

    @Inject
    public ProductRepositoryImpl(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable add(Product product) {
        return dao.insert(ProductMapper.toEntity(product));
    }

    @Override
    public Flowable<List<Product>> getAll() {
        return dao.getAll().map(ProductMapper::toModelList);
    }

    @Override
    public Product findById(int id) {
        ProductEntity product = dao.findById(id);
        return ProductMapper.toModel(product);
    }

    @Override
    public Completable update(Product product) {
        return dao.update(ProductMapper.toEntity(product));
    }

    @Override
    public Completable deleteById(int id) {
        return dao.deleteById(id);
    }
}
