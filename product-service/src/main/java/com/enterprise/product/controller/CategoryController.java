package com.enterprise.product.controller;


import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.enums.ResponseStatus;
import com.enterprise.product.dto.request.CreateCategoryRequest;
import com.enterprise.product.dto.request.UpdateCategoryRequest;
import com.enterprise.product.dto.response.CategoryResponse;
import com.enterprise.product.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// @RestController: Spring ko batata hai ki ye REST API Controller hai. Iske methods ke return objects directly JSON response mein convert hote hain.
@RestController
// Ye Base URL define karta hai. Is class ke sabhi endpoints isi URL se start honge.
@RequestMapping("/api/v1/categories")
// Lombok automatically constructor generate karega final fields ke liye.
// yahan pe Spring constructor injection se CategoryService provide karega.
@RequiredArgsConstructor
public class CategoryController
{
    // ye ek Service layer ka reference hai.
    // Controller business logic khud execute nahi karega, balki ServiceImpl ko kaam delegate karega.
    private final CategoryService categoryService;

    //---------------------------- CREATE CATEGORY ------------------------------//
    // @PostMapping: HTTP POST request handle karega.
    // Complete endpoint: POST /api/v1/categories
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            // @Valid: Yeh Request DTO par lage validation rules ko check karega.
            // @RequestBody: Yeh Incoming JSON ko CreateCategoryRequest Java object mein convert karega.
            @Valid @RequestBody CreateCategoryRequest request,
            // Yeh humlogo ko Current HTTP request ki information provide karega. Isse humlog requested API ka path nikalte hian.
            HttpServletRequest httpRequest)
    {
        // Yahan pe humlogo ka Controller -> Service layer ko call karta hai aur ServiceImpl category create karke CategoryResponse return karta hai.
        CategoryResponse response = categoryService.createCategory(request);

        // yahan pe humlog Common API response wrapper create kar rahe hain.
        // Ismein status, message, actual data, current timeing aur requested path jayega.
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>(
                ResponseStatus.SUCCESS,             // API operation ka result
                "Category created successfully",    // Client ke liye success message
                response,                           // Service se mila CategoryResponse
                LocalDateTime.now(),                // current time
                httpRequest.getRequestURI()         // Requested endpoint ka path
        );

        // HTTP 201 CREATED status ke saath response return hoga.
        // body() mein actual ApiResponse object jayega.
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    //----------------------- UPDATE CATEGORY -------------------------------//
    // @PutMapping("/{categoryId}"): HTTP PUT request handle karega.
    // Complete endpoint: PUT /api/v1/categories/{categoryId}
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            // @PathVariable: Yeh URL se categoryId ki value extract karega.
            // Example: /api/v1/categories/5 mein categoryId = 5.
            @PathVariable Long categoryId,
            // Incoming JSON ko UpdateCategoryRequest object mein convert karega.
            // @Valid request DTO ke validation rules check karega.
            @Valid @RequestBody UpdateCategoryRequest request,
            // Current HTTP request ki information provide karega.
            HttpServletRequest httpRequest)
    {
        // Yahan pe humlog ServiceImpl ko category-ID aur updated data bhej rahe hain.
        // Yahan pe Service category find, validate aur update karegi.
        CategoryResponse response = categoryService.updateCategory(categoryId, request);

        // Yahan pe Updated-category ko common API-response wrapper mein convert kar rahe hain.
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>(
                ResponseStatus.SUCCESS,
                "Category updated Successfully",
                response,
                LocalDateTime.now(),
                httpRequest.getRequestURI()
        );

        // HTTP 200 OK ke saath updated category return kar rahe hain
        return ResponseEntity.ok(apiResponse);
    }

    //------------------------ GET CATEGORY BY ID -----------------------------
    // @GetMapping("/{categoryId}"): Yeh HTTP GET request handle karta hain.
    // Complete endpoint: GET /api/v1/categories/{categoryId}
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            // Yahan pe URL se category ID extract hogi.
            @PathVariable Long categoryId,
            // Requested endpoint ka path lene ke liye.
            HttpServletRequest httpRequest)
    {
        // Yahan pe ServiceImpl se koi particular category fetch kar rahe hain.
        CategoryResponse response = categoryService.getCategoryById(categoryId);

        // Fetched category ko common API-response wrapper mein rakh rahe hain.
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>(
                ResponseStatus.SUCCESS,
                "Category Fetched Successfully",
                response,
                LocalDateTime.now(),
                httpRequest.getRequestURI()
        );

        // HTTP 200 OK ke saath category return kar rahe hain.
        return ResponseEntity.ok(apiResponse);
    }

    //---------------------------- GET ALL CATEGORIES ---------------------------
    // @GetMapping: HTTP GET request handle karega.
    // Complete endpoint: GET /api/v1/categories
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategory(
            // Current request ka path lene ke liye.
            HttpServletRequest httpRequest)
    {
        // ServiceImpl se saari categories ki List fetch kar rahe hain.
        // List<CategoryResponse> ka matlab multiple CategoryResponse objects.
        List<CategoryResponse> response = categoryService.getAllCategory();

        // constructor ke through response
        // List ko common API response wrapper mein rakh rahe hain.
//        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>(
//                ResponseStatus.SUCCESS,
//                "Categories fetched successfully",
//                response,
//                LocalDateTime.now(),
//                httpRequest.getRequestURI()
//        );

        // BUilder ke through response
        // List ko common API response wrapper mein rakh rahe hain.
        ApiResponse<List<CategoryResponse>> apiResponse = ApiResponse.<List<CategoryResponse>>builder()
                .status(ResponseStatus.SUCCESS)
                .message("Categories fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .path(httpRequest.getRequestURI())
                .build();

        // HTTP 200 OK ke saath saari categories return kar rahe hain.
        return ResponseEntity.ok(apiResponse);

    }

}
