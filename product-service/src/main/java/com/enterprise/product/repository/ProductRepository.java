package com.enterprise.product.repository;

import com.enterprise.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
                    // JpaRepository<Product, Long> — standard CRUD operations.
public interface ProductRepository extends JpaRepository<Product, Long> {

    // findBySku() — SKU se product fetch karega.
    Optional<Product> findBySku(String sku);

    // existsBySku() — duplicate SKU check karega.
    Boolean existsBySku(String sku);

    // findByCategoryId() — particular category ke products fetch karega.
    List<Product> findByCategoryId(Long categoryId);

}













