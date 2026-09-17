package com.ecommerce.auth.client;

// common-lib ka common response wrapper
import com.ecommerce.common.dto.ApiResponse;

// Auth → User Service bhejne wala request DTO
import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;

// User Service se receive hone wala actual profile DTO
import com.ecommerce.common.integration.user.UserProfileResponse;

import lombok.RequiredArgsConstructor;

// Generic response ko properly read karne ke liye
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.stereotype.Component;

// HTTP request bhejne ke liye Spring ka RestClient
import org.springframework.web.client.RestClient;


// Spring is class ka object automatically banayega
@Component

// Lombok automatically constructor bana dega
// UserServiceClient(RestClient restClient)
@RequiredArgsConstructor

// Ye actual HTTP client class hai.inlea Naam bhi intentionally:UserServiceClient rakha hai.
// Iska matlab(UserServiceClient) Auth Service ke andar ek client hai jo User Service se baat kar sakta hai.
// Important: Ye User Service nahi hai. Ye sirf User Service ko HTTP request bhejne wala client hai.
public class UserServiceClient {

    // Ye actual Spring HTTP client object hai.
    // Iske through hum: (POST, GET, PUT, DELETE) jaise HTTP requests bhej sakte hain.
    // hamare case mein: Auth Service -> POST HTTP -> User Service.
    private final RestClient restClient;



    // Ye method User Service mein profile create karne ke liye hai.
    // Is method ko Auth Service registration ke time call karta hai.
    // request ke andar hoga: authUserId = 5,  name = "Asif",  phone = null
    public UserProfileResponse createUserProfile(
            CreateUserFromAuthRequest request) {

        // User Service ka complete response: ApiResponse -> data -> UserProfileResponse
        // Isliye ApiResponse<UserProfileResponse> likh rahe hain.
        // Yahan hum bol rahe hain: "Mujhe User Service ko HTTP POST request bhejni hai."
        ApiResponse<UserProfileResponse> response = restClient.post()

                // User Service ke /internal endpoint ko POST request. Ye batata hai request kahan bhejni hai.
                .uri("http://localhost:8082/api/v1/users/internal")

                // request object JSON body mein convert hokar jayega
                // Example: { "authUserId": 5, "name": "Asif", "phone": null }
                .body(request)

                // User Service se response receive karo.
                // method mein receive kar rahe hain, usko HTTP request ke body mein bhej rahe hain.
                .retrieve()

                // Java mein ApiResponse<UserProfileResponse>.class possible nahi hai.
                // Isliye ParameterizedTypeReference use karke Spring ko exact generic type bata rahe hain.
                // "Jo HTTP response aa raha hai, usko ApiResponse samjho aur uske data ke andar UserProfileResponse hai."
                // curli bracket {} ka use  kyun hua.Spring mein generic type information preserve karne ke liye ye standard pattern hai.
                .body(new ParameterizedTypeReference<ApiResponse<UserProfileResponse>>() {});

        // ApiResponse ke andar jo actual data hai wahi UserProfileResponse hai.
        // Isliye cast karne ki zarurat nahi: (UserProfileResponse) ❌
        return response.getData();
    }
}