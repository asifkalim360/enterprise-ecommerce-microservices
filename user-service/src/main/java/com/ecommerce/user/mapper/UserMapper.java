package com.ecommerce.user.mapper;

import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;
import com.ecommerce.user.dto.request.CreateUserRequest;
import com.ecommerce.user.dto.request.UpdateUserRequest;
import com.ecommerce.user.dto.response.UserResponse;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Create ke time: Client se aaye DTO ko Entity me convert kar rahe hain.
    // CreateUserRequest DTO  to User Entity Conversion.
    // Normal User creation
    public User toEntity(CreateUserRequest request)
    {
        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        // New user by default ACTIVE hoga.
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    // Auth se profile creation
    public User toEntityFromAuth(CreateUserFromAuthRequest request) {

        User user = new User();

        user.setAuthUserId(request.getAuthUserId());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    // Update ke time: DTO ki values existing Entity me set kar rahe hain.
    // UpdateUserRequest DTO to Existing User Entity Conversion.
    public void updateEntity(User user, UpdateUserRequest request)
    {
        if(request.getName() != null)
        {
            user.setName(request.getName());
        }
        if(request.getPhone() != null)
        {
            user.setPhone(request.getPhone());
        }
    }

    // Response ke time: Database se mili Entity ko response DTO me convert kar rahe hain.
    // User Entity to UserResponse DTO Conversion.
    public UserResponse toResponse(User user)
    {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
