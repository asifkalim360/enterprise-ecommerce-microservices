package com.ecommerce.common.exception;

/**
 *  Jab koi requested resource system me available naa ho toh ye exception execute hota hai.
 **/
public class ResourceNotFoundException extends BusinessException {

    /**
     *  Resource se related message pass karega ye.
     **/

    public ResourceNotFoundException(String message)
    {
        super(message);
    }

}
