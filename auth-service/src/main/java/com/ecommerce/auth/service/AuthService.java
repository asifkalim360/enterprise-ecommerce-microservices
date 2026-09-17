package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.request.RegisterRequest;
import com.ecommerce.auth.dto.response.AuthResponse;

/**
 *  Auth businees contract.
 **/
public interface AuthService {

    // User registration:
    AuthResponse register(RegisterRequest request);
}
