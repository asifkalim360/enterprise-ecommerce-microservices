package com.ecommerce.auth.exception;

// Common-lib se hamara standard ErrorResponse import kar rahe hain.
// 403 ka response bhi isi common error format me client ko bhejna hai.
import com.ecommerce.common.dto.ErrorResponse;

// ObjectMapper Java object ko JSON format me convert karne ke liye use hota hai.
import com.fasterxml.jackson.databind.ObjectMapper;

// ServletException interface ke method signature me use ho raha hai.
import jakarta.servlet.ServletException;

// Current HTTP request ko access karne ke liye HttpServletRequest use hota hai.
import jakarta.servlet.http.HttpServletRequest;

// HTTP response ko modify karne ke liye HttpServletResponse use hota hai.
import jakarta.servlet.http.HttpServletResponse;

// Lombok ka @RequiredArgsConstructor automatically constructor generate karega.
// Isse final ObjectMapper field constructor ke through inject ho jayega.
import lombok.RequiredArgsConstructor;

// HTTP status codes jaise 403 Forbidden ko represent karne ke liye use hota hai.
import org.springframework.http.HttpStatus;

// Jab authenticated user ke paas required permission/authority nahi hoti,
// Spring Security AccessDeniedException throw karta hai.
import org.springframework.security.access.AccessDeniedException;

// Access denied hone par custom handling karne ke liye ye interface implement kar rahe hain.
import org.springframework.security.web.access.AccessDeniedHandler;

// Is annotation ki wajah se Spring is class ko automatically Bean bana dega.
// Isliye SecurityConfig me is handler ko inject kar sakte hain.
import org.springframework.stereotype.Component;

// Java I/O operation ke liye IOException use hota hai.
import java.io.IOException;
import java.time.LocalDateTime;


// Spring ko bata rahe hain ki is class ko Spring Bean ke roop me manage karo.
@Component

// Lombok automatically required-arguments wala constructor generate karega.
// Yahan final ObjectMapper ke liye constructor automatically ban jayega.
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler
{
    // ObjectMapper ko class ke andar store kar rahe hain.
    // Iska use ErrorResponse object ko JSON me convert karne ke liye hoga.
    private final ObjectMapper objectMapper;


    // AccessDeniedHandler interface ke handle() method ko override kar rahe hain.
    // Jab authenticated user ko resource access karne ki permission nahi hogi,
    // Spring Security automatically is method ko call karega.
    @Override
    public void handle(
            // Jis API endpoint ko user access karna chahta hai uski request yahan milegi.
            HttpServletRequest request,

            // Client ko bhejne wala HTTP response yahan milega.
            HttpServletResponse response,

            // Access deny hone ka reason/exception yahan milega.
            AccessDeniedException accessDeniedException)

    // Response likhte waqt IOException aa sakti hai.
    // Servlet processing ke dauran ServletException bhi aa sakti hai.
            throws IOException, ServletException
    {
        // Client ko bhejne ke liye common ErrorResponse object create kar rahe hain.
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                // HTTP 403 Forbidden ko response ke status field me set kar rahe hain.
                .status(HttpStatus.FORBIDDEN.value())

                // 403 ka standard reason phrase "Forbidden" set kar rahe hain.
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())

                // Client ko batayenge ki requested resource access karne ki permission nahi hai.
                .message("Access denied.")

                // Jis API endpoint par access deny hua uska path response me set kar rahe hain.
                .path(request.getRequestURI())

                // Builder se final ErrorResponse object create kar rahe hain.
                .build();


        // Actual HTTP response ka status code 403 Forbidden set kar rahe hain.
        response.setStatus(HttpStatus.FORBIDDEN.value());


        // Client ko bata rahe hain ki response JSON format me hai.
        response.setContentType("application/json");


        // ObjectMapper ErrorResponse Java object ko JSON me convert karke
        // HTTP response ke writer me likh raha hai.
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}


//Iska flow samjho:
//Authenticated User -> Request -> Spring Security -> Authorization Check -> Permission/Roles insufficient ->
// CustomAccessDeniedHandler -> ErrorResponse -> HTTP 403.
//Response: { "timestamp": "2026-08-20T21:07:00", "status": 403, "error": "Forbidden", "message": "Access denied.", "path": "/api/v1/admin/users" }
//401 vs 403 ko ekdum clear rakho
//Situation Handler    Status:
//User authenticated hi nahi hai toh ->  CustomAuthenticationEntryPoint  401
//User authenticated hai but permission nahi hai  ->  CustomAccessDeniedHandler  403
//Ab dono classes ready hain.