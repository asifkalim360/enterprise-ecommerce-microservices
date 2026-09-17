package com.ecommerce.auth.service;

// LoginRequest = Login API se aane wala data.
// Isme email aur password hoga.
import com.ecommerce.auth.dto.request.LoginRequest;

// LoginResponse = Login successful hone ke baad
// client ko bhejne wala response.
// Isme accessToken aur tokenType hoga.
import com.ecommerce.auth.dto.response.LoginResponse;


// Is interface ko hum "Login Service" bhi samajh sakte hain.
//
// Iska kaam:
// Login ka contract define karna.
//
// Actual login logic yahan nahi likhenge.
// Actual logic AuthenticationServiceImpl class mein likhenge.
public interface AuthenticationService {


    // login() method login ka contract define kar raha hai.
    // Input: LoginRequest request → email + password
    // Output: LoginResponse → accessToken + tokenType
    // Matlab: LoginRequest login() -> LoginResponse
    LoginResponse login(LoginRequest request);


}