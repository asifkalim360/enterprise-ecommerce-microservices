package com.ecommerce.auth.security;

// Hamare database/entity wala User import kar rahe hain.
// Is User object ke andar id, name, email, password, role etc. honge.
import com.ecommerce.auth.entity.User;

// GrantedAuthority = Spring Security ka interface.
// Ye batata hai ki user ke paas kaunsi authority/permission hai.
import org.springframework.security.core.GrantedAuthority;

// SimpleGrantedAuthority = GrantedAuthority ka ready-made implementation.
// Hum iske through user ka ROLE set karenge.
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// UserDetails = Spring Security ka interface.
// Spring Security ko user ki information isi format mein chahiye.
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/*
 UserDetails
 → User ki security information represent karta hai:
   username/email
   password
   authorities/roles

 UserDetailsService
 → Database se user ko find/load karta hai,
   usually email/username ke basis par.

 Simple word mein:
 UserDetails        = "User ki security information Spring Security ke format mein do"
 UserDetailsService = "User ko database se lao"
*/

// CustomUserDetails hamari custom class hai.
// Ye Spring Security ke UserDetails interface ko implement kar rahi hai.
//
// Matlab:
// Spring Security bolega:
// "Mujhe UserDetails chahiye"
//
// Hum bolenge:
// "Theek hai, CustomUserDetails ke through deta hoon."
public class CustomUserDetails implements UserDetails {


    // Hamare database wala User object yahan store hoga.
    //
    // Is User ke andar:
    // user.getEmail()
    // user.getPassword()
    // user.getRole()
    // etc. available honge.
    private final User user;


    // Constructor
    //
    // Jab CustomUserDetails ka object banega,
    // tab database wala User object isme pass hoga.
    //
    // Example:
    // new CustomUserDetails(user);
    public CustomUserDetails(User user)
    {
        // Received User object ko class ke 'user' variable mein store kar rahe hain.
        this.user = user;
    }


    // UserDetails interface ka method.
    //
    // Iska kaam:
    // User ki authorities/roles Spring Security ko dena.
    //
    // Example:
    // ADMIN → ROLE_ADMIN
    // USER  → ROLE_USER
    // MANAGER → ROLE_MANAGER
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // List.of() se authorities ki list bana rahe hain.
        //
        // new SimpleGrantedAuthority(...)
        // Spring Security ko ek authority object deta hai.
        return List.of(

                // User ka role database se le rahe hain.
                //
                // Agar:
                // user.getRole().name()
                //
                // return kare:
                // "ADMIN"
                //
                // to:
                // "ROLE_" + "ADMIN"
                //
                // = "ROLE_ADMIN"
                //
                // Spring Security mein role convention ke liye
                // ROLE_ prefix commonly use hota hai.
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                )
        );
    }


    // UserDetails interface ka method.
    //
    // Spring Security jab user ka password maangega,
    // ye method database wale User ka password return karega.
    @Override
    public String getPassword() {

        // User entity se password lekar return kar rahe hain.
        return user.getPassword();
    }


    // UserDetails interface ka method.
    //
    // Spring Security "username" ke naam se user ko identify karta hai.
    //
    // Hamare project mein username ki jagah email use ho raha hai.
    @Override
    public String getUsername() {

        // Isliye database wale User ka email return kar rahe hain.
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }
}


/*
========================================================
IMPORTANT CONCEPT
========================================================

Spring Security:
        ↓
Mujhe UserDetails chahiye
        ↓
CustomUserDetails
        ↓
Database wala User
        ↓
Spring Security ko milega:

Username  → user.getEmail()
Password  → user.getPassword()
Role      → ROLE_ADMIN / ROLE_USER / ROLE_MANAGER


========================================================
UserDetails vs UserDetailsService
========================================================

UserDetails
→ User ki security information ka FORMAT/CONTRACT.

UserDetailsService
→ Database se user ko LOAD/FIND karne ka kaam.


========================================================
HAMARE PROJECT ME
========================================================

CustomUserDetails
→ User entity ko Spring Security ke format mein convert karega.

CustomUserDetailsService
→ Email ke basis par User ko database se nikalega.


security/
│
├── CustomUserDetails.java
│
└── CustomUserDetailsService.java


========================================================
LOGIN FLOW
========================================================

Client
  ↓
email + password
  ↓
AuthenticationManager
  ↓
CustomUserDetailsService
  ↓
Database se User
  ↓
CustomUserDetails
  ↓
Spring Security ko:
  email
  password
  role
  ↓
Password verify
  ↓
Authentication successful
  ↓
JWT generate
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