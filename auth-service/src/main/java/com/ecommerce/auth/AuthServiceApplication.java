package com.ecommerce.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  Ye Auth-service ka entry point hai.
 *  JVM sabse pahle esi class ka main() method execute krta hai.
 **/

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args)
    {
        // Yahin se spring boot application start hoti hai.
        SpringApplication.run(AuthServiceApplication.class);
    }

}
