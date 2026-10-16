package com.ecommerce.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Refresh Token Entity
 *
 * Refresh token ko Auth DB mein store karenge.
 *
 * Important:
 * - Access Token short-lived hota hai.
 * - Refresh Token long-lived hota hai.
 * - Refresh token ko DB mein store karne se hum:
 *      1. revoke kar sakte hain
 *      2. logout kar sakte hain
 *      3. rotation implement kar sakte hain
 *      4. expired token ko identify kar sakte hain
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kis Auth User ka refresh token hai uske liye userId liye hain. kyunki Yahan pe direct JPA @ManyToOne relationship nahi banayenge.
    // kyunki Microservice architecture mein User/Auth identity ko loosely coupled rakhna hai. Isliye sirf Auth User ID ko store karenge.
    @Column(nullable = false)
    private Long userId;

    // Actual refresh token value.
    // Production mein ideally raw token ke bajay hashed token store karna aur bhi secure approach hota hai.
    // Abhi learning/project flow ko simple rakhte hue token value store kar rahe hain.
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    // Refresh token kab expire hoga.
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Logout ya token rotation ke time token revoke kiya ja sakta hai.
    @Builder.Default
    @Column(nullable = false)
    private Boolean revoked = false;

    // Token create hone ka time.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        // Entity first time DB mein save hone par timestamp set hoga.
        this.createdAt = LocalDateTime.now();

        // Safety: Agar builder se revoked value nahi di gayi to false rahega.
        if (this.revoked == null) {
            this.revoked = false;
        }
    }
}