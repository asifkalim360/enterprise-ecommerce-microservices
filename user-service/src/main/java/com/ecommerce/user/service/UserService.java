package com.ecommerce.user.service;

import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;
import com.ecommerce.user.dto.request.CreateUserRequest;
import com.ecommerce.user.dto.request.UpdateUserRequest;
import com.ecommerce.user.dto.response.UserResponse;

public interface UserService {

    public UserResponse createUser(CreateUserRequest request);

    public UserResponse createUserFromAuth(CreateUserFromAuthRequest request);

    public UserResponse getUserById(Long id);

    public UserResponse updateUser(Long id, UpdateUserRequest request);

    public void deactivateUser(Long id);

}
