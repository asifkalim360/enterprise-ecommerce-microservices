package com.enterprise.product.repository;

import com.enterprise.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // findByNameIgnoreCase() — category name se search karne ke liye.
    Optional<Category> findByNameIgnoreCase(String name);

    //existsByNameIgnoreCase() — duplicate category name check karne ke liye.
    boolean existsByNameIgnoreCase(String name);

}
