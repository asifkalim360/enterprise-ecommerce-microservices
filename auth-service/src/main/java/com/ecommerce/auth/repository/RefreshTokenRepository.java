package com.ecommerce.auth.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>
{
    // Refresh token value se token find karenge.
    Optional<RefreshToken> findByToken(String token);

    // User ke saare refresh token find karne ke liye.
    // Future me multiple device/session handle karne me useful hoga.
    // Ek user ke multiple devices/sessions ho sakte hain.
    List<RefreshToken> findAllByUserId(Long userId);

    // Rotation ke waqt row token pe write lock lagega.
    // jisse ki concurrent requests me same token ko ek sath rotate karne ka risk kam hota hai.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RefreshToken r WHERE r.token = :token")
    Optional<RefreshToken> findByTokenForUpdate(@Param("token") String token);

}
