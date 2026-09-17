package com.ecommerce.common.integration.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserFromAuthRequest {

    private Long authUserId;

    private String name;

    private String phone;
}

/**
 *  Is DTO ka kaam sirf itna hai:
 * Auth Service
 *       │
 *       │ ye data bhejega
 *       │
 *       ▼
 *  User Service
 * authUserId   = 5
 * name         = Asif
 * phone        = 9876543210
 */