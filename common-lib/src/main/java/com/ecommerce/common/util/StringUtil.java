package com.ecommerce.common.util;

/**
 *  String related reusable helper methods.
 **/
// Class:- class Final hone ke karan Inheritance allowed nahi hoga.
public final class StringUtil {

    /*
    *   Object Creation Rokne ke liye private constructor banaya gaya hai.
    */

    private StringUtil(){   // Constructor:- constructor private hone ke karan Object creation nahi ho payega.
    }

    /*
     *  Yahan pe Null ya blank String check Karenge.
     */
    // // Static Method: static hone ki wajah se ye method Direct call hoga Object ki zarurat nahi padegi.
    public static boolean isBlank(String value)
    {
        return value == null || value.isBlank();
    }
}
