package com.ecommerce.auth.repository;

import com.ecommerce.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>
{
    // Refresh token value se token find karenge.
    Optional<RefreshToken> findByToken(String token);

    //User ke saare refresh token find karne ke liye.
    // Future me multiple device/session handle karne me useful hoga.
    // Ek user ke multiple devices/sessions ho sakte hain.
    List<RefreshToken> findByUserId(Long userId);
}
