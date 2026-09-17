package com.ecommerce.auth.exception;

// Hamare common-lib se ErrorResponse import kar rahe hain.
// 401 ka response isi common format me client ko bhejna hai.
import com.ecommerce.common.dto.ErrorResponse;

// ObjectMapper Java object ko JSON format me convert karne ke liye use hota hai.
import com.fasterxml.jackson.databind.ObjectMapper;

// ServletException AuthenticationEntryPoint ke method signature me use ho raha hai.
import jakarta.servlet.ServletException;

// HTTP request ko access karne ke liye HttpServletRequest use hota hai.
import jakarta.servlet.http.HttpServletRequest;

// HTTP response ko modify karne ke liye HttpServletResponse use hota hai.
import jakarta.servlet.http.HttpServletResponse;

// HTTP status codes jaise 401, 403, 400, 500 ko represent karne ke liye use hota hai.
import org.springframework.http.HttpStatus;

// Jab authentication fail hoti hai ya authentication available nahi hoti,
// Spring Security AuthenticationException provide karta hai.
import org.springframework.security.core.AuthenticationException;

// Authentication fail hone par Spring Security isi interface ke through
// hamare custom handler ko call karega.
import org.springframework.security.web.AuthenticationEntryPoint;

// Is annotation ki wajah se Spring is class ko automatically Bean bana dega.
// Isliye SecurityConfig me is class ko inject kar sakte hain.
import org.springframework.stereotype.Component;

// Java I/O operation ke liye IOException use hota hai.
import java.io.IOException;


// Spring ko bata rahe hain ki is class ko Spring Bean ke roop me manage karo.
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // ObjectMapper ko class ke andar store kar rahe hain.
    // Iska use ErrorResponse object ko JSON response me convert karne ke liye hoga.
    private final ObjectMapper objectMapper;


    // Constructor injection ke through ObjectMapper receive kar rahe hain.
    // Spring automatically ObjectMapper ka Bean provide karega.
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper)
    {
        // Constructor se mila ObjectMapper class ke field me store kar rahe hain.
        this.objectMapper = objectMapper;
    }


    // AuthenticationEntryPoint interface ka method override kar rahe hain.
    // Jab Spring Security ko authentication problem milegi,
    // tab ye method automatically execute hoga.
    @Override
    public void commence(
            // Jis API ko client access karna chahta hai uski request yahan milegi.
            HttpServletRequest request,

            // Client ko bhejne wala HTTP response yahan milega.
            HttpServletResponse response,

            // Authentication fail hone ka reason/exception yahan milega.
            AuthenticationException authException)

    // Response likhte waqt IOException aa sakti hai.
            throws IOException, ServletException
    {

        // Client ko bhejne ke liye hamara common ErrorResponse object create kar rahe hain.
        ErrorResponse errorResponse = ErrorResponse.builder()

                // HTTP status 401 ko response ke status field me set kar rahe hain.
                .status(HttpStatus.UNAUTHORIZED.value())

                // 401 ka standard name "Unauthorized" set kar rahe hain.
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())

                // Client ko batayenge ki authentication required hai.
                .message("Authentication required.")

                // Jis API endpoint par request aayi thi uska path response me set kar rahe hain.
                .path(request.getRequestURI())

                // Builder se final ErrorResponse object create kar rahe hain.
                .build();


        // Actual HTTP response ka status code 401 Unauthorized set kar rahe hain.
        response.setStatus(HttpStatus.UNAUTHORIZED.value());


        // Client ko bata rahe hain ki response JSON format me hai.
        response.setContentType("application/json");


        // ObjectMapper ErrorResponse Java object ko JSON me convert karke
        // HTTP response ke writer me likh raha hai.
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}


// Is class ka kaam sirf ek hai:
//Jab request me authentication nahi ho ya fhr authentication fail ho gayi ho toh → 401 Unauthorized response dena hota hai.
// Ye code kya kar raha hai?
// Flow samjho: Unauthenticated Request -> Spring Security -> AuthenticationException -> CustomAuthenticationEntryPoint -> ErrorResponse -> HTTP 401
// Aur response tumhare common ErrorResponse format me jayega:
//{ "timestamp": "2026-08-20T17:21:00", "status": 401, "error": "Unauthorized", "message": "Authentication required.", "path": "/api/v1/user/profile" }
//Ek important cheez Hum yahan: ObjectMapper use kar rahe hain.
// Isliye raw JSON: response.getWriter().write("{...}"); nahi likhna padega.
// ObjectMapper automatically Java ke ErrorResponse object ko JSON me convert karega.