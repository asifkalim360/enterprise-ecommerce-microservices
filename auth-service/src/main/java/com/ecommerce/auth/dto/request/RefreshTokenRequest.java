package com.ecommerce.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest
{
    // Client login response se mila raw refresh token bhejega.
    // Hash client nahi bhejega; hashing Auth Service ke andar hogi.
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
