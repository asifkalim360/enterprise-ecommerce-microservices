package com.ecommerce.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  Password Encoder Configuration.
 **/
@Configuration
public class PasswordConfig {

    // PasswordEncoder Bean.
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}



//Sabse important distinction:::::
//Common-lib mein:
//JWT banana/parse/validate → JwtService
//Bearer token read karke SecurityContext establish karna → JwtAuthenticationFilter
//Auth Service mein:
//Login user ko DB se load karna → CustomerUserDetailsService
//User ka Spring Security representation → CustomerUserDetails
//Password hashing/verification → PasswordConfig
//Auth Service ke endpoints/rules → SecurityConfig
//Isliye Auth Service ko sirf SecurityConfig.java rakhna hai — ye sahi nahi hai.
