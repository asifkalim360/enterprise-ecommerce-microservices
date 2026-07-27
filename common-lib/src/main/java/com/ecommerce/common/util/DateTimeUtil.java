package com.ecommerce.common.util;      // Package:- Saari reusable utility classes yahan rahengi.

import java.time.LocalDateTime;

/**
 *  Date aur Time Related Reusable Helper Methods.
 **/
public final class DateTimeUtil {       // Class:- class Final hone ke karan Inheritance allowed nahi hoga.


    /*
    *   Object Creation rokne ke liye private constructor bnaya gaya hai.
    */

    private DateTimeUtil(){     // Constructor:- constructor private hone ke karan Object creation nahi ho payega.
    }

    /*
    *   Current date aur time return karenge.
    */
    // Static Method: static hone ki wajah se ye method Direct call hoga Object ki zarurat nahi padegi.
    public static LocalDateTime getCurrentDateTime()
    {
        // Return: Current system date-time return karega.
        // Baad me isi method ko timezone support, formatting, testing aur clock abstraction ke liye improve kar sakte hain.
        return LocalDateTime.now();
    }
}
/*
*** pending task.
Real enterprise me future me
UTC support
Time Zone support
Date Formatting
Parsing
Testing ke liye injectable Clock
jaise improvements add karenge.
*/
