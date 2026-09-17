package com.ecommerce.user.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.integration.user.CreateUserFromAuthRequest;
import com.ecommerce.user.dto.request.CreateUserRequest;
import com.ecommerce.user.dto.request.UpdateUserRequest;
import com.ecommerce.user.dto.response.UserResponse;
import com.ecommerce.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.common.enums.ResponseStatus;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request, HttpServletRequest httpRequest)
    {
        UserResponse user = userService.createUser(request);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(ResponseStatus.SUCCESSS)
                .message("User created successfully")
                .data(user)
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/internal")
    public ResponseEntity<ApiResponse<UserResponse>> createUserFromAuth(
            @Valid @RequestBody CreateUserFromAuthRequest request,
            HttpServletRequest httpRequest)
    {
        UserResponse user = userService.createUserFromAuth(request);

        ApiResponse<UserResponse> response =
                ApiResponse.<UserResponse>builder()
                .status(ResponseStatus.SUCCESSS)
                .message("User profile create successfully")
                .data(user)
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id, HttpServletRequest httpRequest)
    {

        UserResponse user = userService.getUserById(id);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(ResponseStatus.SUCCESSS)
                .message("User fetched successfully")
                .data(user)
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest)
    {
        UserResponse user = userService.updateUser(id, request);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(ResponseStatus.SUCCESSS)
                .message("User update successfully")
                .data(user)
                .path(httpRequest.getRequestURI())
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(
            @PathVariable Long id, HttpServletRequest httpRequest )
    {
        userService.deactivateUser(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(ResponseStatus.SUCCESSS)
                .message("User deactivated successfully")
                .data(null)
                .path(httpRequest.getRequestURI())
                .build();
        return ResponseEntity.ok(response);
    }

}
