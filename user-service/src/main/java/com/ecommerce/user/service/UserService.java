package com.ecommerce.user.service;

import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;
import com.ecommerce.user.dto.request.CreateUserRequest;
import com.ecommerce.user.dto.request.UpdateUserRequest;
import com.ecommerce.user.dto.response.UserResponse;

public interface UserService {

    public UserResponse createUser(CreateUserRequest request);

    public UserResponse createUserFromAuth(CreateUserFromAuthRequest request);

    // ADMIN kisi bhi user ki profile dekh sakta hai.
    public UserResponse getUserById(Long id);

    // ADMIN kisi bhi profile ko update kar sakta hai.
    public UserResponse updateUser(Long id, UpdateUserRequest request);

    // Logged-in user ki apni profile.
    UserResponse getMyProfile(Long authUserId);

    // Logged-in user sirf apni profile update karega.
    UserResponse updateMyProfile(Long authUserId, UpdateUserRequest request);

    public void deactivateUser(Long id);

}
