package com.dannyjulian.productservice.repository;

import com.dannyjulian.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
