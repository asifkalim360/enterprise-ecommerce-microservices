package com.ecommerce.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 *  Registration request DTO
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    // User ka full name
    @NotBlank(message = "Name is Required")
    @Size(min=3, max=100)
    private String name;

    // Email Validation
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Size(max=150)
    private String email;

    // Strong Password required
    @NotBlank(message="Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be strong"
    )
    private String password;


}
