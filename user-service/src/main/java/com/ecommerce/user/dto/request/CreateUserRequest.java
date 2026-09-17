package com.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(max=100, message="Name must not exceed 100 characters")
    private String name;

    @Pattern(
            regexp = "[0-9]{10}$",
            message="Phone number must contain exactly 10 digits"
    )
    private  String phone;

}
