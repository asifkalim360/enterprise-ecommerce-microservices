package com.ecommerce.common.security;

import io.jsonwebtoken.JwtException;

// ****************************************************
// Servlet Classes
// ****************************************************
// FilterChain:Current filter ke baad jo next filters hain, unko request forward karne ke liye use hota hai.
import jakarta.servlet.FilterChain;
// ServletException: Filter processing ke time servlet-level exception ke liye.
import jakarta.servlet.ServletException;
// HttpServletRequest:Client ki incoming HTTP request. Isse hum Authorization header read karenge.
import jakarta.servlet.http.HttpServletRequest;
// HttpServletResponse: Server ka outgoing HTTP response.
import jakarta.servlet.http.HttpServletResponse;

// ****************************************************
// Spring Security Classes
// ****************************************************
// UsernamePasswordAuthenticationToken: Spring Security ke andar authenticated user ko represent karta hai.
// Important: Naam dekhkar confuse mat hona. Yahan hum actual username/password login nahi kar rahe.
// JWT successfully validate hone ke baad hum is object ko authenticated user ki identity ke roop me SecurityContext me rakhte hain.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// SecurityContextHolder: Current request ke authenticated user ki information SecurityContext ke andar store karta hai.
// Baad me application me: @PreAuthorize, hasRole(), hasAuthority(), authentication.getName() jaise mechanisms isi authentication information ko use kar sakte hain.
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
// UsernameNotFoundException: Agar UserDetailsService ko requested user database me nahi milta hai to ye exception aa sakta hai.
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Spring Component: Is class ko Spring Bean bana deta hai. Iska matlab Spring is filter ka object khud create/manage karega.
import org.springframework.stereotype.Component;

// OncePerRequestFilter: Spring ka special filter. Guarantee karta hai ki given request ke processing me filter normally ek baar execute ho.
import org.springframework.web.filter.OncePerRequestFilter;

// Java IO
import java.io.IOException;
import java.util.List;

// Lombok: Constructor automatically generate karega.
// Isse manually constructor likhne ki zarurat nahi:
// public JwtAuthenticationFilter( JwtService jwtService, UserDetailsService userDetailsService) { ... }
import lombok.RequiredArgsConstructor;


// ****************************************************
// @Component
// ****************************************************
// Spring is class ko Bean ke roop me register karega. Lekin ek important architectural point:
// Ye class common-lib me hai. Jab koi microservice common-lib ko dependency ke roop me use karegi to
// Spring Boot component scanning ke through is Bean ko discover kar sakta hai, provided package scanning
// configuration appropriate ho Isliye common security infrastructure reusable ban raha hai.

//@Component


// ****************************************************
// @RequiredArgsConstructor
// ****************************************************
// Dono final fields ke liye constructor dependency injection automatically generate karega:
// Spring dependency injection ke through dono dependencies provide karega.
@RequiredArgsConstructor

// ****************************************************
// Filter Class
// ****************************************************
// Ye hamara custom JWT authentication filter hai.
// Iska main kaam: HTTP Request -> Authorization Header -> Bearer JWT Token -> Token se username ->
//  -> UserDetailsService se user -> JWT validate -> Authentication Object -> SecurityContext -> Protected API.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // ****************************************************
    // JwtService
    // ****************************************************
    // JwtService JWT ke actual operations handle karta hai: token se username extract karna, token validate karna, expiration check karna, JWT signature verify karna.
    // Ye business logic nahi hai. Ye reusable JWT infrastructure hai. Isi wajah se humne ise common security package me rakhne ka decision liya hai.
    private final JwtService jwtService;


    // ****************************************************
    // doFilterInternal()
    // ****************************************************
    // Ye method har incoming HTTP request ke liye execute hota hai jab filter SecurityFilterChain me registered hai.
    // Example request: GET /api/v1/users/10
    // Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        // ****************************************************
        // STEP 1: Authorization Header Read Karna
        // ****************************************************
        // Client request me normally: Authorization: Bearer <JWT> hota hai.
        // request.getHeader("Authorization") isi header ko read karta hai.
        String authHeader = request.getHeader("Authorization");

        // ****************************************************
        // STEP 2: Check — Authorization Header Hai Ya Nahi?
        // ****************************************************
        // Agar Authorization header hi nahi hai ya fhr header "Bearer " se start nahi ho raha ho to hum JWT authentication attempt nahi karenge.
        // Example: Authorization: Basic abc123 -> Ye hamara JWT Bearer token nahi hai.
        // IMPORTANT: Yahan request ko reject nahi kar rahe hian Bas keh rahe hain: "Mere paas JWT nahi hai, next filter ko request de do."
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // ****************************************************
            // STEP 3: Next Filter Ko Request Forward
            // ****************************************************
            // filterChain.doFilter() -> current filter ke baad next filter ko request deta hai. Agar yahan return nahi karenge to neeche ka code bhi execute ho jayega.
            filterChain.doFilter(request, response);
            // Method ko yahin stop kar dete hain.
            return;
        }

        // ****************************************************
        // STEP 4: "Bearer " Remove Karke Actual JWT Lena
        // ****************************************************
        // Header: "Bearer eyJhbGciOiJIUzI1NiJ9...", "Bearer " = 7 characters, substring(7) ke baad: "eyJhbGciOiJIUzI1NiJ9..."
        // yani actual JWT token mil jayega.
        String token = authHeader.substring(7);

        // ****************************************************
        // STEP 5: JWT Processing
        // ****************************************************
        // JWT related operations ko try block me rakha hai kyunki token invalid ho sakta hai.
        // Example: token expired, signature invalid, malformed token, token tampered
        try {
            // ****************************************************
            // STEP 6: JWT Se Username Extract
            // ****************************************************
            // JwtService token ko parse karega, signature verify karega, aur JWT ke subject se username return karega.
            // Example JWT: { "sub": "asif@gmail.com", "iat": ..., "exp": ... }
            // extractUsername(token) -> "asif@gmail.com"
            String username = jwtService.extractUsername(token);

            // ****************************************************
            // 7. JWT se role extract karo
            // ****************************************************
            String role = jwtService.extractRole(token);

            // ****************************************************
            // STEP 8: Username Valid Hai?
            // AND
            // Current Request Already Authenticated Nahi Hai?
            // ****************************************************
            // Pehli condition: username != null Matlab JWT se valid username mila.
            // Dusri condition:SecurityContextHolder.getContext().getAuthentication() == null Matlab current request already authenticated nahi hai.
            // Agar authentication already present hai to unnecessary authentication dobara create nahi karenge.
            if (username != null &&
                    role != null &&
                    jwtService.isTokenValid(token) &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // ****************************************************
                // 9. JWT role ko Spring Authority mein convert karo
                // ****************************************************
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                // ****************************************************
                // STEP 10: Authentication Object Create
                // ****************************************************
                // Spring Security ko ab batana hai: "Ye user authenticate ho chuka hai."
                // UsernamePasswordAuthenticationToken : yahan authentication representation ka kaam karta hai.
                // First argument:
                // username → authenticated user ki information
                // Second argument: null -> password yahan verify nahi ho raha. JWT already identity proof provide kar raha hai.
                // Third argument: List.of(authority) → user ke roles/permissions.
                // Example: ROLE_USER , ROLE_ADMIN
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(authority)
                            );

                // =========================================
                // STEP 12: SecurityContext Me Authentication
                // =========================================
                // Ye is filter ka SABSE IMPORTANT step hai. Ab Spring Security ko officially bata rahe hain:
                // "Current request authenticated hai. aur authenticated user ye hai."
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }
        // ====================================================
        // STEP 13: Invalid JWT Handle
        // ====================================================
        // Agar JWT invalid hua: JwtException - Agar username database/user source me nahi mila: UsernameNotFoundException: to yahan control aayega.
        catch(JwtException | IllegalArgumentException ex)
        {
            // =================================================
            // Invalid JWT -> Authentication Set Nahi Hui
            // =================================================
            // Hum yahan manually response return nahi kar rahe.
            // Iska matlab: SecurityContext me authentication set nahi hua.
            // Aage SecurityConfig ka: .anyRequest().authenticated() -> decide karega ki protected endpoint ko access milega ya Spring Security authentication failure handle karega.
            // IMPORTANT: Production me logging/monitoring add ki ja sakti hai,lekin sensitive JWT/token ko logs me print nahi karna chahiye.
        }


        // ******************************************************************
        // STEP 14: Request Ko Next Filter/controller Ki taraf Forward krega
        // ******************************************************************
        // Chahe: JWT valid tha ya fhir JWT invalid tha ya fhir authentication create nahi hua fhir bhi filter chain ko continue karna hai.
        // Agar JWT valid tha: SecurityContext me authentication already present hai.
        // Agar JWT invalid tha: Authentication absent rahegi aur Spring Security protected resource ke liye later decision lega.
        filterChain.doFilter(request, response);
    }
}



