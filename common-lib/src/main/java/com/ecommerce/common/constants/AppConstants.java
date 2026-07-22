package com.ecommerce.common.constants;

/**
 * Project me use hone wale common constants.
 * */

// final ki wajah se Inheritance stop ho jayega.
public final class AppConstants {

    // private eslea kiya taaki Object create naa ho sake es class ka.
    private AppConstants() // private = Object creation stop.
    {

    }

    // Success Message ke liye.
    // Public=Har jagah accessible, Static = Object nahi banana, Final = Immutable.
    public static final String SUCCESS_MESSAGE = "Operation completed Successfullt";

    // Failed Message ke liye.
    // Public=Har jagah accessible, Static = Object nahi banana, Final = Immutable.
    public static final String FAILED_MESSAGE = "Operation Failed.";

    // Default Pagination Size.
    public static final int DEFAULT_PADE_SIZE = 10;

    // Default page number
    public static final int DEFAULT_PAGE_NUMBER = 0;

}
