package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.client.UserServiceClient;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.auth.dto.request.RegisterRequest;
import com.ecommerce.auth.dto.response.AuthResponse;
import com.ecommerce.auth.entity.Role;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.entity.User;
import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  Auth Service Implimentation.
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    //Constructor Injection.
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    //User Service ko HTTP request bhejne ke liye client.
    private final UserServiceClient userServiceClient;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Duplicate email check:
        if(repository.existsByEmail(request.getEmail()))
        {
            throw new BusinessException("Email already registration.");
        }

        // Auth DB mein sirf authentication-related data save hoga.
        // NOTE: name Auth DB mein ab save nahi hoga.
        // DTO to Entity Conversion:
        User user = User.builder()
//                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        // Auth DB mein user save.
        // save hone ke baad generated Auth User ID mil jayegi.
        User savedUser = repository.save(user);

        // Ab Auth User ki ID + profile information User Service ko bhejenge.
        CreateUserFromAuthRequest userRequest = CreateUserFromAuthRequest.builder()
                //Auth DB ki generated ID. EXAMPLE: Auth User ID = 5.
                .authUserId(savedUser.getId())

                // Name Auth DB mein nahi hai, isliye RegisterRequest se directly User Service ko bhejenge.
                .name(request.getName())

                // RegisterRequest mein abhi phone field nahi hai, isliye filhaal ye null rahega.
                .phone(null)
                .build();

        // Auth Service → HTTP → User Service
        // User Service is authUserId ko apne User DB ke auth_user_id column mein save karega.
        userServiceClient.createUserProfile(userRequest);

        // Registration ka Auth response.
        return AuthResponse.builder()
                .id(savedUser.getId())
             // .name(savedUser.getName())  // Auth User mein name ab hai hi nahi
                .email(savedUser.getEmail())
                .token(null)
                .build();
    }



}
