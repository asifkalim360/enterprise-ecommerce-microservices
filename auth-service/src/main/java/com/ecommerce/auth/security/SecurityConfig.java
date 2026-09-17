package com.ecommerce.auth.security;

import com.ecommerce.auth.exception.CustomAccessDeniedHandler;
import com.ecommerce.auth.exception.CustomAuthenticationEntryPoint;
import com.ecommerce.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
* @Configuration Spring ko bol raha hai ki es class ke andar application configuration hai.
  Application startup par Spring is class ko detect karega. aur Phir iske @Bean methods ko process karega.
*/
@Configuration

// Spring Security ke web/security support ko enable karta hai.
// Iske through Spring Security ka security infrastructure application mein activate hota hai.
// Note: Modern Spring Boot mein security auto-configuration already kaafi kaam kar deti hai, lekin hum yahan explicitly security configuration define kar rahe hain.
@EnableWebSecurity


// Lombok automatically constructor generate karega.
// Neeche hamara: private final JwtAuthenticationFilter jwtAuthenticationFilter; hai.
// Isliye Spring constructor ke through JwtAuthenticationFilter inject kar dega.
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /* @Bean Spring ko hum bol rahe hain ki es method se jo object return hoga, usko Spring ApplicationContext me Bean ke roop me manage karo.
       Return type(SecurityFilterChain) hai aur Ye actual security filter chain hai jo runtime par requests process karegi.
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        /* HttpSecurity Ye ek configuration builder hai. Iske through hum security rules define karte hain.
            Conceptually: HttpSecurity -> Configure Security -> build() -> SecurityFilterChain
        */
        http
                /*
                * Pehle samjho CSRF kya hai. CSRF ka full form(Cross-Site Request Forgery) hota hai.
                  Traditional browser-based session application me attacker kisi logged-in browser se unwanted request karwane ki koshish kar sakta hai.
                  Example: User bank me logged in -> Malicious website -> Bank transfer request -> Browser automatically session cookie bhej deta hai.

                  # LEKIN REST API + JWT architecture me authentication normally header ke through hoti hai.(Authorization: Bearer <JWT>)
                    Browser automatically Authorization header attach nahi karta. Isliye humare stateless JWT REST architecture me CSRF generally disable kiya jata hai.
                    Important: CSRF ko blindly har application me disable nahi karna chahiye. Agar authentication cookie/session based ho, toh situation different hota hai.
               */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                    authorizeHttpRequests: Yahan se hum authorization rules define kar rahe hain.
                    Matlab: Kaunsi request allowed hai?,  Kaunsi request authenticated honi chahiye?.
               */
                .authorizeHttpRequests(auth -> auth
                        /*
                            Ye URLs ko match karega (/api/v1/auth/**)
                            Matlab: /api/v1/auth/register, /api/v1/auth/login, /api/v1/auth/refresh etc.
                            Abhi authentication endpoints public honge.
                            Swagger: (/swagger-ui/**) || OpenAPI: (/v3/api-docs/**)
                            development/testing ke liye public rakhe hain.
                         */
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        /*
                            permitAll(): Matlab Is matching request ke liye authentication required nahi hai.
                            Example: (POST /api/v1/auth/register) -> Token nahi hai. Still allowed.
                            Important misconception: permitAll() ka matlab ye nahi hai ki Spring Security Filter Chain bypass ho gayi.
                            Request security infrastructure se guzarti hai, lekin authorization rule ke according authentication requirement nahi lagti.
                        */
                        .permitAll()

                        /*
                            anyRequest(): Matlab Jo request upar ke matchers me match nahi hui, usko consider karo.
                            Example: (GET /api/v1/users) -> Ye /api/v1/auth/** ke andar nahi hai to anyRequest() apply hoga.
                        */
                        .anyRequest()

                        /*
                            authenticated(): Matlab Is endpoint ko access karne ke liye authenticated user required hai.
                            Abhi Part 12 me JWT implement nahi hua hai. Isliye protected API ko proper authentication ke bina access karenge to 401 Unauthorized mil sakta hai.
                            Jab JWT authentication add karne ke baad: JWT -> Authentication -> SecurityContext -> authenticated() -> Controller hoga.
                        */
                        .authenticated()
                )
                /*
                Ye JWT architecture ke liye bahut important hai.
                 # Stateful:  Traditional session architecture:
                    (Login -> Server Session Create -> Session ID -> Client Cookie -> Next Request ->Server Session Lookup) Server user session maintain karta hai.
                 # Stateless:  JWT architecture
                    (Login -> JWT -> Client stores token -> Every Request -> Authorization Header -> Server validates JWT) Server traditional login session maintain nahi karta.
                    Isliye: SessionCreationPolicy.STATELESS
                */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .exceptionHandling(exception ->
                        // Spring Security ki security-related exceptions ko handle karne ki configuration.
                        exception
                                // User authenticated nahi hai → CustomAuthenticationEntryPoint call hoga → 401
                                .authenticationEntryPoint(authenticationEntryPoint)
                                // User authenticated hai lekin permission nahi hai → CustomAccessDeniedHandler → 403
                                .accessDeniedHandler(accessDeniedHandler)
                )


                // =====================================================
                // JWT FILTER KO SECURITY FILTER CHAIN MEIN ADD KARNA ho
                // =====================================================
                // Yahan hum apne custom JwtAuthenticationFilter ko Spring Security ki existing filter chain mein add kar rahe hain.
                // Lekin important hai ki JWT filter ko UsernamePasswordAuthenticationFilter se PEHLE execute karaya jaye.
                .addFilterBefore(

                // Hamara custom JWT filter. Ye request se: Authorization: Bearer <JWT> nikalega aur JWT validate karega.
                jwtAuthenticationFilter,

                // Reference point Matlab: "JwtAuthenticationFilter ko UsernamePasswordAuthenticationFilter se pehle execute karo."
                UsernamePasswordAuthenticationFilter.class);



        /*
        Last line: return http.build(); -> Ye extremely important hai.
        Ab tak:
        http
           .csrf(...)
           .authorizeHttpRequests(...)
           .sessionManagement(...);
        abhi tk Humlog sirf configuration ko build kar rahe the.
        lekin  build() ke baad: HttpSecurity configuration -> SecurityFilterChain -> Spring Bean -> Runtime Security
        */
        return http.build();
    }
//----------------------------------------------------------------------------

    // @Bean ka matlab:
    // Spring is method ko execute karke jo object return hoga,
    // us object ko Spring Container mein Bean bana kar rakhega.
    //
    // Yahan return hone wala object:
    // AuthenticationManager
    //
    // Baad mein Login API mein hum AuthenticationManager ko
    // use karke email + password verify karenge.
    @Bean
    public AuthenticationManager authenticationManager(

            // AuthenticationConfiguration Spring Security ki configuration
            // ko represent karta hai.
            //
            // Iske andar Spring Security ka AuthenticationManager
            // already configure/create karne ki information hoti hai.
            AuthenticationConfiguration configuration)

        // AuthenticationManager obtain karte waqt Exception aa sakta hai,
        // isliye method throws Exception kar raha hai.
            throws Exception {


        // configuration ke andar se Spring Security ka
        // AuthenticationManager nikal rahe hain.
        //
        // getAuthenticationManager()
        // → configured AuthenticationManager return karega.
        //
        // return karne ke baad @Bean ki wajah se
        // ye AuthenticationManager Spring Container mein available ho jayega.
        return configuration.getAuthenticationManager();
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










