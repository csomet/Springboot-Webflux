package com.csomet.springboot.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.csomet.springboot.webflux.app.models.documents.Product;

public interface IProductDao extends ReactiveMongoRepository<Product, String> {}
