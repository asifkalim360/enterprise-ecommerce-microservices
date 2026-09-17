package com.ecommerce.auth.service.impl;


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


// @Service
// Spring is class ka object automatically create karega.
@Service

// @RequiredArgsConstructor
// Lombok automatically ye constructor bana dega:
//
// public AuthenticationServiceImpl(
//         AuthenticationManager authenticationManager,
//         JwtService jwtService
// ) {
//     this.authenticationManager = authenticationManager;
//     this.jwtService = jwtService;
// }
//
// Isliye hume manually constructor likhne ki zarurat nahi.
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    // AuthenticationManager ka object.
    //
    // Iska kaam:
    // email + password ko authenticate/verify karwana.
    //
    // Ye wahi AuthenticationManager hai
    // jo humne SecurityConfig mein @Bean se expose kiya tha.
    private final AuthenticationManager authenticationManager;


    // Hamari custom JwtService.
    //
    // Authentication successful hone ke baad
    // isi service se JWT token generate karenge.
    private final JwtService jwtService;


    // AuthenticationService interface ka login() method implement kar rahe hain.
    @Override
    public LoginResponse login(LoginRequest request) {


        // YAHAN ACTUAL AUTHENTICATION START HOTI HAI.
        //
        // authenticationManager.authenticate(...)
        // Spring Security ko bol raha hai:
        //
        // "Is email aur password ko verify karo."
        //
        // Agar credentials correct hain:
        // → Authentication object return hoga.
        //
        // Agar wrong hain:
        // → AuthenticationException throw hogi.
        Authentication authentication = authenticationManager.authenticate(


                // Email + password ko Spring Security ke
                // authentication object mein convert kar rahe hain.
                //
                // UsernamePasswordAuthenticationToken ka naam
                // "Username" hai, lekin hamare project mein
                // username ke place par EMAIL use ho raha hai.
                new UsernamePasswordAuthenticationToken(

                        // LoginRequest se email nikal rahe hain.
                        request.getEmail(),

                        // LoginRequest se password nikal rahe hain.
                        request.getPassword()
                )
        );


        // Authentication successful ho chuki hai.
        //
        // Ab authentication object ko JwtService ke paas bhej rahe hain.
        //
        // JwtService authenticated user ki information use karke
        // JWT access token generate karegi.
        String accessToken = jwtService.generateToken(authentication);

        // Authentication successful hone ke baad Access JWT generate karo
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();



        // Ab client ko LoginResponse return kar rahe hain.
        //
        // Builder Pattern ka use ho raha hai.
        //
        // LoginResponse mein:
        // accessToken
        // tokenType
        // set kar rahe hain.
        return LoginResponse.builder()

                // Generated JWT token ko response ke
                // accessToken field mein set kar rahe hain.
                .accessToken(accessToken)


                // Token ka type "Bearer" hai.
                //
                // Client future protected API requests mein
                // header bhejega:
                //
                // Authorization: Bearer <token>
                .tokenType("Bearer")

                // Builder se final LoginResponse object create kar raha hai.
                .build();
    }



}