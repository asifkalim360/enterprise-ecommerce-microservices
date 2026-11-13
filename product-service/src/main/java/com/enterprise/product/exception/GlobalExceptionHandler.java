package com.enterprise.product.exception;

import com.ecommerce.common.dto.ErrorResponse;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
// @RestControllerAdvice ek special annotation hai.
// Ye application ke multiple Controllers ko globally handle karta hai.
// Controller mein koi exception throw hoti hai,
// toh ye class us exception ko catch karke appropriate response return kar sakti hai.

public class GlobalExceptionHandler {


    // Resource not found: HTTP 404

    @ExceptionHandler(ResourceNotFoundException.class)
    // @ExceptionHandler batata hai ki ye method
    // ResourceNotFoundException ko handle karega.
    //
    // Example:
    // Product ID 10 database mein nahi mila,
    // toh Service ResourceNotFoundException throw karegi.
    //
    // Spring us exception ko is method tak bhej dega.

    public ResponseEntity<ErrorResponse> handleResourceNotFound(

            ResourceNotFoundException ex,
            // ex mein actual thrown exception object aayega.
            // ex.getMessage() se exception ka message milega.
            // Example: "Product not found with ID: 10"

            HttpServletRequest request
            // Current HTTP request ki information provide karta hai.
            // Isse hum request ka URL/path nikal sakte hain.
    ) {

        // ErrorResponse ka object Builder pattern se create kar rahe hain.
        ErrorResponse errorResponse = ErrorResponse.builder()

                // HTTP 404 ka numeric status code set kar rahe hain.
                // value() se 404 integer milega.
                .status(HttpStatus.NOT_FOUND.value())

                // NOT_FOUND ka readable name milega: "Not Found"
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())

                // Exception se actual error message le rahe hain.
                .message(ex.getMessage())

                // Jis URL par error aayi, uska path set kar rahe hain.
                // Example: /api/v1/products/10
                .path(request.getRequestURI())

                // Builder complete karke ErrorResponse object bana rahe hain.
                .build();


        // Client ko HTTP 404 status ke saath error response return kar rahe hain.
        return ResponseEntity.status(HttpStatus.NOT_FOUND)

                // Response body mein hamara custom ErrorResponse jayega.
                .body(errorResponse);
    }


    // Validation error: HTTP 400

    @ExceptionHandler(ValidationException.class)
    // Ye method ValidationException ko handle karega.
    // Example:
    // Product create/update karte waqt business validation fail ho gayi.

    public ResponseEntity<ErrorResponse> handleValidation(

            ValidationException ex,
            // Thrown ValidationException ka object.
            // ex.getMessage() se actual validation error milega.

            HttpServletRequest request
            // Current request ka path lene ke liye.
    ) {

        // Custom ErrorResponse object create kar rahe hain.
        ErrorResponse errorResponse = ErrorResponse.builder()

                // HTTP 400 Bad Request ka numeric status code.
                .status(HttpStatus.BAD_REQUEST.value())

                // Readable error name: "Bad Request"
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())

                // Exception ka actual message.
                .message(ex.getMessage())

                // Current request ka URL path.
                .path(request.getRequestURI())

                // ErrorResponse object complete.
                .build();


        // Client ko HTTP 400 status ke saath error response bhej rahe hain.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)

                // Custom JSON response body.
                .body(errorResponse);
    }


    // General business error: HTTP 400

    @ExceptionHandler(BusinessException.class)
    // Ye method BusinessException ko handle karega.
    //
    // Example:
    // Product already exists with SKU.
    // Ya koi business rule violate hua.
    //
    // Service layer se BusinessException throw ho sakti hai.

    public ResponseEntity<ErrorResponse> handleBusinessException(

            BusinessException ex,
            // Actual BusinessException object.
            // Iske through exception ka message access karenge.

            HttpServletRequest request
            // Current HTTP request ka path lene ke liye.
    ) {

        // Custom ErrorResponse object create kar rahe hain.
        ErrorResponse errorResponse = ErrorResponse.builder()

                // HTTP 400 Bad Request ka numeric status code.
                .status(HttpStatus.BAD_REQUEST.value())

                // Readable error name: "Bad Request"
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())

                // Business exception ka actual message.
                // Example: "Product already exists with SKU: ABC-001"
                .message(ex.getMessage())

                // Current request ka URL path.
                .path(request.getRequestURI())

                // Builder complete.
                .build();


        // HTTP 400 ke saath custom error response return kar rahe hain.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)

                // Response body mein ErrorResponse jayega.
                .body(errorResponse);
    }

// Bean Validation error: HTTP 400

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Ye annotation batata hai ki agar
    // MethodArgumentNotValidException throw hoti hai,
    // toh Spring is method ko execute karega.
    // Example: @NotBlank validation fail hui, @Positive validation fail hui, @NotNull validation fail hui.
    // @Valid ki wajah se validation fail hone par
    // Spring ye exception throw kar sakta hai.
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(

            MethodArgumentNotValidException ex,
            // ex mein actual exception object aayega.
            // Is object ke through hum validation errors ki details nikalenge.

            HttpServletRequest request
            // Current HTTP request ki information milegi.
            // Isse hum request ka URL path nikalenge.

    ) {

        // Validation fail hone wale first field ka error message le rahe hain.
        FieldError fieldError = ex.getBindingResult().getFieldError();

        // getBindingResult() validation results provide karta hai.
        // getFieldError() first field ka validation error return karta hai.
        //
        // Example:
        // Agar productName blank hai,
        // toh fieldError mein productName ki error details hongi.


        String errorMessage = fieldError != null

                // Agar fieldError available hai,
                // toh uska default validation message le lenge.
                // Example: "Product name is required"

                ? fieldError.getDefaultMessage()

                // Agar fieldError null hai,
                // toh fallback message use hoga.
                : "Invalid request data";


        // Custom ErrorResponse object create kar rahe hain.
        ErrorResponse errorResponse = ErrorResponse.builder()

                // HTTP 400 ka numeric status code set kar rahe hain.
                .status(HttpStatus.BAD_REQUEST.value())

                // HTTP 400 ka readable name: "Bad Request"
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())

                // Validation se mila actual error message set kar rahe hain.
                .message(errorMessage)

                // Current request ka URL path set kar rahe hain.
                .path(request.getRequestURI())

                // Builder complete karke ErrorResponse object bana rahe hain.
                .build();


        // Client ko HTTP 400 Bad Request status return kar rahe hain.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)

                // Response body mein hamara custom ErrorResponse jayega.
                .body(errorResponse);
    }

}