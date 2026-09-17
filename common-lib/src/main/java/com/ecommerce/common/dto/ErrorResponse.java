package com.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *  Standard Error Response for All Microservices.
 *  global exception me jo bhi error aayega usko esi format me client ko serve kiya jayega.
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    // Error Generate hone ka time kya hai.
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // HTTP ka Status code
    private int status;

    // Error Name
    private String error;

    // Error Messages
    private String message;

    // Requested API Path
    private String path;

}
