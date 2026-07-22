package com.ecommerce.common.dto;


import com.ecommerce.common.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Sabhi microservces ke liye common API Response Wrapper.
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    // Response successful ya failed
    private ResponseStatus status;

    // User friendly message
    private String message;

    // Actual response payload
    private T data;

    // Response generate hone ka time
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // API endpoint path
    private String path;

}
