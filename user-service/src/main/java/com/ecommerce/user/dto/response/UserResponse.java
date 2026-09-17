package com.ecommerce.user.dto.response;

import com.ecommerce.user.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String phone;
    private UserStatus status;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

}
