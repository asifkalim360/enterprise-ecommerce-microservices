package com.ecommerce.auth.dto.request;

import lombok.*;

// @Getter
// Lombok automatically email aur password ke getter methods bana dega.
// Internally:
// getEmail()
// getPassword()
@Getter

// @Setter
// Lombok automatically email aur password ke setter methods bana dega.
// Internally:
// setEmail()
// setPassword()
@Setter

// @NoArgsConstructor
// Ye automatically empty/default constructor banata hai.
// Internally:
// public LoginRequest() {}
@NoArgsConstructor

// @AllArgsConstructor
// Ye automatically saare fields wala constructor banata hai.
// Internally:
// public LoginRequest(String email, String password) { ... }
@AllArgsConstructor

// LoginRequest ek DTO (Data Transfer Object) hai.
// Iska kaam Login API se email aur password receive karna hai.
public class LoginRequest {

    // User ka login email yahan receive hoga.
    private String email;

    // User ka login password yahan receive hoga.
    private String password;
}
