package com.ecommerce.common.exception;

/**
 *  // Request Validation related custom exception.
 **/
public class ValidationException extends BusinessException{

    /**
    *   Validation error message receive akrenge.
    **/

    public ValidationException(String message)
    {
        super(message);
    }

}
