package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.entity.RefreshToken;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.service.RefreshTokenService;
import com.ecommerce.common.exception.BusinessException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

// Login API se email + password receive karne wala DTO.
import com.ecommerce.auth.dto.request.LoginRequest;

// Login successful hone ke baad client ko bhejne wala response DTO.
import com.ecommerce.auth.dto.response.LoginResponse;

// JWT token generate karne wali hamari custom service.
//import com.ecommerce.auth.jwt.JwtService;
import com.ecommerce.common.security.JwtService;

// AuthenticationService interface.
// Ye class isi interface ke login() method ko implement karegi.
import com.ecommerce.auth.security.CustomUserDetails;
import com.ecommerce.auth.service.AuthenticationService;


// Spring Security ka main authentication component.
//
// Iska kaam:
// email/password ko verify karwana.
import org.springframework.security.authentication.AuthenticationManager;

// Email + password ko Spring Security ke authentication request
// ke format mein convert karta hai.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// Authentication successful hone ke baad authenticated user ki
// security information is object mein hoti hai.
import org.springframework.security.core.Authentication;

// @Service class ko Spring Bean bana deta hai.
import org.springframework.stereotype.Service;

// Lombok automatically constructor generate karta hai
// jisme saare final fields honge.
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService
{
    // AuthenticationManager ka object hai.
    // Iska kaam: email + password ko authenticate/verify karwana.
    // Ye wahi AuthenticationManager hai jo humne SecurityConfig me @Bean se expose kiya tha.
    private final AuthenticationManager authenticationManager;

    // Hamari custom JwtService. Authentication successful hone ke baad isi service se JWT token generate karenge.
    private final JwtService jwtService;

    // Auth DB se user ki ID retrieve karne ke liye.
    private final UserRepository userRepository;

    // Refresh token generate aur validate karne wali service.
    private final RefreshTokenService refreshTokenService;

    // Email se Spring Security UserDetails load karne ke liye.
    // Project mein UserDetailsService ka existing Spring bean hona chahiye.
    private final UserDetailsService userDetailsService;


    // AuthenticationService interface ka login() method implement kar rahe hain.
    @Override
    public LoginResponse login(LoginRequest request) {


        // YAHAN SE ACTUAL AUTHENTICATION START HOTI HAI.
        // authenticationManager.authenticate(...) Spring Security ko bol raha hai: "Is email aur password ko verify karo."
        // Agar credentials correct hain: → Authentication object return hoga || Agar wrong hain: → AuthenticationException throw hogi.
        // Step 1: Email aur password authenticate karo
        Authentication authentication = authenticationManager.authenticate(
                // Email + password ko Spring Security ke authentication object mein convert kar rahe hain.
                // UsernamePasswordAuthenticationToken ka naam "Username" hai, lekin hamare project mein username ke place par EMAIL use ho raha hai.
                new UsernamePasswordAuthenticationToken(
                        // LoginRequest se email nikal rahe hain.
                        request.getEmail(),
                        // LoginRequest se password nikal rahe hain.
                        request.getPassword()
                )
        );

        // Authentication successful ho chuki hai. Ab authentication object ko JwtService ke paas bhej rahe hain.
        // JwtService authenticated user ki information use karke JWT access token generate karegi.
        // Step 2: Authentication successful hone par Access Token generate karo
        String accessToken = jwtService.generateToken(authentication);

        // Step 3: Auth DB se logged-in user ka record nikalo
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new BusinessException("User not found"));

        // Step 4: Refresh Token generate karo
        // Raw token client ko milega; database mein uska hash save hoga.
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

//        // Authentication successful hone ke baad Access JWT generate karo
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Ab client ko LoginResponse return kar rahe hain. Builder Pattern ka use ho raha hai.
        // LoginResponse mein:accessToken, tokenType set kar rahe hain.
        // Step 5: Dono tokens client ko response mein bhejo
        return LoginResponse.builder()
                // Generated JWT token ko response ke accessToken field mein set kar rahe hain.
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                // Token ka type "Bearer" hai. Client future protected API requests mein header bhejega: Authorization: Bearer <token>
                .tokenType("Bearer")
                // Builder se final LoginResponse object create kar raha hai.
                .build();
    }

    @Override
    public LoginResponse refreshAccessToken(String rawRefreshToken) {

        // yahan pahle Raw token ka hash check hoga(matlab token validate hoga aur Auth user-ID niklega).
        // invalid, revoked ya expired token yahin reject hoga.
        RefreshToken storedToken = refreshTokenService.verifyRefreshToken(rawRefreshToken);

        // Auth database se user load karo.
        // Refresh token se Auth user ID milegi;User Service ko call nahi karna.
        User user = userRepository.findById(storedToken.getUserId()).orElseThrow(() -> new BusinessException("User not found"));

        // Latest user details aur authorities load karo.
        // Current user ki security details reload kar rahe hain.
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // Password dobara verify nahi karna; refresh token pehle validate ho chuka hai.
        // Existing authorities ke saath Authentication object banayenge.
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        // Existing common-lib JwtService se naya access token generate hoga.
        String newAccessToken = jwtService.generateToken(authentication);

        // IMPORTANT: purana refresh token revoke hoga aur new token generate hoga.
        // Agar token beech me kisi aur request ne use kar liya to rotation reject hogi aur response return nahi hoga.
        String newRefreshToken = refreshTokenService.rotateRefreshToken(rawRefreshToken);

        // Client ko purana refresh token dobara nahi dena hai islea newRefreshToken ko refreshToken me use karenge.
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }



}