package com.ecommerce.auth.dto.response;

import lombok.*;

/**
 *  Registration/Login response.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    // User ID
    private Long id;

    // User Name
    private String name;

    // User Email
    private String email;

    // JWT Token:
    //Registration me null ho sakta hai. Login ke baad value aayegi.
    private String token;

}
