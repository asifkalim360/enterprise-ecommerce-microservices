package com.ecommerce.auth.dto.response;

// Lombok ke saare common annotations import ho jayenge:
// @Getter, @Setter, @ToString, @EqualsAndHashCode,
// @RequiredArgsConstructor, @NoArgsConstructor,
// @AllArgsConstructor, @Builder, etc.
import lombok.*;

// @Data ek saath multiple Lombok features provide karta hai:
// 1. @Getter       → har field ka getter
// 2. @Setter       → har field ka setter
// 3. @ToString     → toString() method
// 4. @EqualsAndHashCode → equals() aur hashCode()
// 5. RequiredArgsConstructor → required fields ka constructor
//
// Yahan fields final nahi hain, isliye practically getter/setter,
// toString, equals/hashCode main benefits hain.
@Data

// Empty constructor automatically generate karega.
// Internally:
// public LoginResponse() {}
@NoArgsConstructor

// Saare fields wala constructor automatically generate karega.
// Internally:
// public LoginResponse(String accessToken, String tokenType) {
//     this.accessToken = accessToken;
//     this.tokenType = tokenType;
// }
@AllArgsConstructor

// Builder Pattern provide karega.
// Isse object ko readable way me create kar sakte hain:
//
// LoginResponse.builder()
//     .accessToken("abc.xyz...")
//     .tokenType("Bearer")
//     .build();
@Builder
public class LoginResponse {

    // Login successful hone ke baad JWT access token yahan rahega.
    // Example: eyJhbGciOiJIUzI1NiJ9...
    private String accessToken;

    // Login successful hone ke baad JWT refresh token yahan rahega.
    // Example: kdiwdgwbe423n3i43ni23h2fnciOiJIUzI1NiJ9...
    private String refreshToken;

    // Token ka type batata hai.JWT ke saath normally: "Bearer"
    // Client baad me request bhejega: Authorization: Bearer <accessToken>
    private String tokenType;
}
