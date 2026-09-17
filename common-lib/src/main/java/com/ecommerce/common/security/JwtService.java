package com.ecommerce.common.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Common JWT Service.
 * JWT generate, extract aur validate karne ke liye sabhi microservices mein reusable implementation.
 */
public class JwtService
{

    private final SecretKey secretKey;
    private final long expiration;

    // Constructor
    public JwtService(String secret, long expiration)
    {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
        this.expiration = expiration;
    }

    // JWT GENERATION
    public String generateToken(Authentication authentication)
    {
        String username = authentication.getName();

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(()-> new IllegalStateException("Authenticated user has no role"));

        Date now = new Date();

        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    // EXTRACT USERNAME / EMAIL
    public String extractUsername(String token)
    {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // EXTRACT ROLE.
    public String extractRole(String token)
    {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // TOKEN VALIDATION.
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // TOKEN EXPIRATION.
    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)
    {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    // TOKEN VALIDATION WITHOUT USERDETAILS.
    public boolean isTokenValid(String token)
    {
        try
        {
            return !isTokenExpired(token);
        }
        catch (Exception ex)
        {
            return  false;
        }
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

// ============================================================
// JWT Exception
// ============================================================
// JwtException JWT token se related errors ko represent karta hai.
// Jaise: Token invalid hai, Token tampered hai, Token parse nahi ho raha, Signature invalid hai, Token expired hai.

