package com.ecommerce.auth.repository;

import com.ecommerce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *  User Database Operations.
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Email se user searcing.
    Optional<User> findByEmail(String email);

    // Email already exist karta hai ya nahi.
    boolean existsByEmail(String email);
}
