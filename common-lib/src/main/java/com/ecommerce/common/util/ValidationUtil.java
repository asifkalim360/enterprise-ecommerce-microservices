package com.ecommerce.common.util;


import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
  * Valiation related reusable helper methods.
  **/
// Class:- class Final hone ke karan Inheritance allowed nahi hoga.
public final class ValidationUtil {

    /*
    * Object Creation Rokne ke liye private constructor banaya gaya hai.
    * Constructor:- constructor private hone ke karan Object creation nahi ho payega.
    */
    private ValidationUtil(){}

    /*
     *  Email Validation Pattern.
     *  private: Sirf isi class ke andar use hoga || static: Ek hi object banega || final: Reference change nahi hogi.
     *  esme Pattern.compile(...)JVM regex ko parse karti hai.Internal representation banati hai.Future matching fast ho jata hai.
     */
    private static final Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /*
    * Indian Mobile number validation pattern.
    * Mobile Validation: Hum Indian mobile support kar rahe hain.
    */
    private static final Pattern MOBILE_PATTERN = java.util.regex.Pattern.compile("^[6-9][0-9]{9}$");

    /*
    *  Password Valiation pattern.
    * Password Validation: Abhi enterprise level minimum rule lenge.
    * Ye perfect nahi hai, lekin foundation ke liye achha hai.Baad me special character policy bhi add karenge.
    */
    private static final Pattern PASSWORD_PATTERN = java.util.regex.Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    /*
     *  CHECKING EMAIL VALID HAI YA NAHI.
     */
    public static boolean isValidEmail(String email)        // Method static hai to Direct call hoga.
    {
        // BLANK CHECKING: Dhayan dene wali baat hai ki Humne pichhle class ka StringUtil reuse kiya hai.
        // Yahi common-lib ka purpose hai. Ek utility doosri utility ko safely use kar sakti hai.
        if(StringUtil.isBlank(email))
        {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /*
    *  CHECKING MOBILE NUMBER VALID HAI YA NAHI.
    */
    public static boolean isValidMobile(String mobile)      // Method static hai to Direct call hoga.
    {
        // BLANK CHECKING: Dhayan dene wali baat hai ki Humne pichhle class ka StringUtil reuse kiya hai.
        // Yahi common-lib ka purpose hai. Ek utility doosri utility ko safely use kar sakti hai.
        if(StringUtil.isBlank(mobile))
        {
            return false;
        }
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    /*
     *  CHECKING PASSWORD VALID HAI YA NAHI.
     */
    public static boolean isValidPassword(String password)      // Method static hai to Direct call hoga.
    {
        // BLANK CHECKING: Dhayan dene wali baat hai ki Humne pichhle class ka StringUtil reuse kiya hai.
        // Yahi common-lib ka purpose hai. Ek utility doosri utility ko safely use kar sakti hai.
        if(StringUtil.isBlank(password))
        {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }



}
