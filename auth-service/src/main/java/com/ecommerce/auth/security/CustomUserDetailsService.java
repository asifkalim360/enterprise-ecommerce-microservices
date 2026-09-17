package com.ecommerce.auth.security;

// Hamari User entity.
// Database mein jo actual user data hai, usko represent karti hai.
import com.ecommerce.auth.entity.User;

// UserRepository ke through database se User ko find karenge.
import com.ecommerce.auth.repository.UserRepository;

// UserDetails = Spring Security ka interface.
// Iske format mein hum user ki security information denge.
import org.springframework.security.core.userdetails.UserDetails;

// UserDetailsService = Spring Security ka interface.
// Iska kaam database se user ko load/find karna hai.
import org.springframework.security.core.userdetails.UserDetailsService;

// Agar user database mein nahi mila,
// to ye exception throw karenge.
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// @Service class ko Spring ke container mein Bean bana deta hai.
import org.springframework.stereotype.Service;


/*
 UserDetails
 → User ki security information represent karta hai:
   email/username
   password
   authorities/roles

 UserDetailsService
 → Database se user ko find/load karta hai.

 Simple:

 UserDetails
 = "User ko Spring Security ke format mein do"

 UserDetailsService
 = "User ko database se lao"
*/


// @Service ki wajah se Spring is class ka object automatically create karega
// aur ApplicationContext mein Bean ke roop mein rakhega.
//
// Baad mein Spring Security isi service ko use karega
// jab login ke time user ko database se find karna hoga.

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // UserRepository ke through database se User ko search karenge.
    //
    // final ka matlab:
    // ek baar constructor mein value assign hone ke baad
    // is variable ko dobara change nahi kar sakte.
    private final UserRepository userRepository;


    // Constructor Injection
    //
    // Spring automatically UserRepository ka Bean yahan inject karega.
    //
    // Matlab Spring roughly:
    //
    // new CustomUserDetailsService(userRepository)
    //
    // jaisa kaam karega.
    public CustomUserDetailsService(UserRepository userRepository)
    {
        // Constructor se mila UserRepository
        // class ke userRepository variable mein store kar rahe hain.
        this.userRepository = userRepository;
    }


    // UserDetailsService interface ka method.
    //
    // IMPORTANT:
    // Method ka naam "loadUserByUsername" hai,
    // lekin hamare project mein username ki jagah EMAIL use ho raha hai.
    //
    // Isliye parameter ka naam "email" rakha hai.
    //
    // Spring Security login ke time is method ko call karega.
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {


        // Database se email ke basis par user find kar rahe hain.
        //
        // findByEmail(email)
        // UserRepository ka method hai.
        //
        // Ye Optional<User> return karta hai.
        //
        // Example:
        //
        // email = "asif@gmail.com"
        //
        // Database:
        // ┌────┬───────────────┬──────────────┐
        // │ id │ email         │ password     │
        // ├────┼───────────────┼──────────────┤
        // │ 1  │ asif@gmail.com│ $2a$10...    │
        // └────┴───────────────┴──────────────┘
        //
        // User mil gaya → User object
        // User nahi mila → Optional.empty()
        User user = userRepository
                .findByEmail(email)

                // Agar user nahi mila,
                // to UsernameNotFoundException throw hoga.
                //
                // Matlab:
                // "Bhai is email wala user database mein hai hi nahi."
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found:" + email
                ));


        // Database se mila User directly return nahi kar rahe.
        //
        // Kyun?
        //
        // Kyunki Spring Security ko normal User entity nahi,
        // UserDetails format chahiye.
        //
        // Isliye:
        //
        // User
        //   ↓
        // CustomUserDetails
        //   ↓
        // UserDetails
        //
        // CustomUserDetails hamari previous class hai.
        return new CustomUserDetails(user);
    }
}


/*
========================================================
AB PURE CLASS KA KAAM
========================================================

Spring Security login ke time bolega:

"Email mila hai:
 asif@gmail.com

Is email wala user database se lao."

             ↓

CustomUserDetailsService
             ↓
loadUserByUsername("asif@gmail.com")
             ↓
UserRepository
             ↓
findByEmail("asif@gmail.com")
             ↓
Database
             ↓
User mil gaya
             ↓
new CustomUserDetails(user)
             ↓
Spring Security ko UserDetails mil gaya.


========================================================
AGAR USER DATABASE ME NAHI MILA?
========================================================

findByEmail(email)
        ↓
Optional.empty()
        ↓
orElseThrow(...)
        ↓
UsernameNotFoundException


========================================================
DONO CLASSES KA CONNECTION
========================================================

CustomUserDetailsService
        │
        │ database se User laata hai
        ↓
     User
        │
        │ convert
        ↓
CustomUserDetails
        │
        │ Spring Security format
        ↓
   UserDetails


========================================================
SIMPLE DIFFERENCE
========================================================

CustomUserDetailsService
→ "Database se user LAO"

CustomUserDetails
→ "Laaye hue user ko Spring Security
   ke FORMAT mein DO"


========================================================
LOGIN FLOW ME EXACT POSITION
========================================================

LoginRequest
(email + password)
        ↓
AuthenticationManager
        ↓
CustomUserDetailsService
        ↓
loadUserByUsername(email)
        ↓
UserRepository
        ↓
Database
        ↓
User
        ↓
CustomUserDetails
        ↓
Password verification
        ↓
Authentication successful
        ↓
JWT Token Generate
        ↓
LoginResponse
(accessToken + tokenType)
*/


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