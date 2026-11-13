package com.enterprise.product.security;

import com.ecommerce.common.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig
{

    // Common-lib ka JWT authentication filter ko inject kar rahe hain.
    // Ye filter incoming request se JWT token read aur validate karega.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Spring Security ki complete filter chain configure karne ke liye Bean.
    // Iske through hum decide karte hain ki kaunsi API public hogi
    // aur kaunsi API ke liye authentication/authorization required hai.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                // CSRF protection disable kar rahe hain.
                // Kyunki Hamlog APIs stateless JWT authentication use kar rahe hain.
                // humlog Browser session/cookie based authentication use nahi kar rahe.
                .csrf(csrf -> csrf.disable())
                // Esme Server user ka session maintain nahi karega.
                // Har request ke saath client ko JWT token bhejna hota hai.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Yahan pe humlog API access permissions define kar rahe hain.
                .authorizeHttpRequests(auth -> auth
                        // Swagger APIs public: Swagger UI aur OpenAPI documentation public hain.
                        // Inhe access karne ke liye JWT token nahi chahiye.
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Product aur Category ki sabhi GET APIs public hain.
                        // Example:
                        // GET /api/v1/products
                        // GET /api/v1/products/1
                        // GET /api/v1/categories
                        // GET /api/v1/categories/1
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/products/**",
                                "/api/v1/categories/**"
                        ).permitAll()

                        // Product APIs: Only ADMIN and MANAGER can create/update
                        // Product create karne ke liye ADMIN ya MANAGER required hai.
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        // Product update karne ke liye ADMIN ya MANAGER required hai.
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        // Category APIs: Only ADMIN and MANAGER can create/update
                        // Category create karne ke liye ADMIN ya MANAGER required hai.
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**")
                        .hasAnyRole("ADMIN", "MANAGER")
                        // Category update karne ke liye ADMIN ya MANAGER required hai.
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        // Baaki APIs ke liye authenticattion required
                        // Upar define ki gayi permissions ke alawa
                        // baaki sabhi requests ke liye valid authentication required hai.
                        .anyRequest().authenticated()
                )

                // Authentication aur authorization errors ko standard JSON format mein handle karenge.
                .exceptionHandling(exception -> exception

                        // 401 Unauthorized:
                        // Jab token missing ho ya valid authentication available na ho.
                        .authenticationEntryPoint((request, response, authException) ->
                        {
                            // HTTP response status 401 set kar rahe hain.
                            response.setStatus(
                                    jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
                            );

                            // Client ko JSON response bhej rahe hain.
                            response.setContentType("application/json");

                            // ObjectMapper Java object ko JSON mein convert karega.
                            com.fasterxml.jackson.databind.ObjectMapper objectMapper =
                                    new com.fasterxml.jackson.databind.ObjectMapper()
                                            .findAndRegisterModules();

                            // Common-lib ke standard ErrorResponse ka object create kar rahe hain.
                            com.ecommerce.common.dto.ErrorResponse errorResponse =
                                    com.ecommerce.common.dto.ErrorResponse.builder()
                                            .status(401)
                                            .error("Unauthorized")
                                            .message("Authentication required or token is invalid")
                                            .path(request.getRequestURI())
                                            .build();

                            // ErrorResponse object ko JSON format mein client ko send kar rahe hain.
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })

                        // 403 Forbidden:
                        // Jab user authenticated hai lekin uske paas required role nahi hai.
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                        {
                            // HTTP response status 403 set kar rahe hain.
                            response.setStatus(
                                    jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN
                            );

                            // Client ko JSON response bhej rahe hain.
                            response.setContentType("application/json");

                            // ObjectMapper Java object ko JSON mein convert karega.
                            com.fasterxml.jackson.databind.ObjectMapper objectMapper =
                                    new com.fasterxml.jackson.databind.ObjectMapper()
                                            .findAndRegisterModules();

                            // Common-lib ke standard ErrorResponse ka object create kar rahe hain.
                            com.ecommerce.common.dto.ErrorResponse errorResponse =
                                    com.ecommerce.common.dto.ErrorResponse.builder()
                                            .status(403)
                                            .error("Forbidden")
                                            .message("You do not have permission to access this resource")
                                            .path(request.getRequestURI())
                                            .build();

                            // ErrorResponse object ko JSON format mein client ko send kar rahe hain.
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })
                )
                // JWT filter ko Spring Security filter chain mein add kar rahe hain.
                // UsernamePasswordAuthenticationFilter se pehle execute hoga.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Configured security rules ke saath SecurityFilterChain return karte hain.
        return http.build();
    }


}
