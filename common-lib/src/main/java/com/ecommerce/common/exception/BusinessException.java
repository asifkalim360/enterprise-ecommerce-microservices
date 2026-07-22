package com.ecommerce.common.exception;

/**
 *  Business related errors ke liye common custom exception.
 **/

public class BusinessException extends RuntimeException{

    /**
     *  Sirf message pass karne wala constructor.
     */

    public BusinessException(String message)
    {
        super(message);
    }
}
