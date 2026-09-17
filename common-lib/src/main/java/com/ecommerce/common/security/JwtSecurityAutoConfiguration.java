package com.ecommerce.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class JwtSecurityAutoConfiguration
{
    @Bean
    public JwtService jwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration)

    {
        return new JwtService(secret, expiration);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService)
    {
        return new JwtAuthenticationFilter(jwtService);
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
