package com.enterprise.product.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.enums.ResponseStatus;
import com.enterprise.product.dto.request.CreateProductRequest;
import com.enterprise.product.dto.request.UpdateProductRequest;
import com.enterprise.product.dto.response.ProductResponse;
import com.enterprise.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

// Ye class REST Controller hai. Ye Client se HTTP requests receive karega.
// Methods se return hone wale objects automatically JSON me convert hoga.
@RestController
// Is controller ka common/base URL define karta hai. Iske andar sabhi endpoints isi URL se start honge.
@RequestMapping("/api/v1/products")
// Lombok automatically constructor generate karega.
// final fields ke liye constructor banega.
// Spring isi constructor ke through ProductService inject karega.
@RequiredArgsConstructor
public class ProductController
{
    // Yahan ProductService ka object declare kiya hai.
    // final hone ki wajah se constructor injection use hoga.
    // Controller business logic khud execute nahi karega, balki Service layer ko responsibility dega.
    private final ProductService productService;

    // HTTP POST request handle karega.
    // Complete endpoint: POST /api/v1/products
    @PostMapping
    // Iska use naya Product create karne ke liye hoga.
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            // Request DTO par lage validation rules check karega. Jaise @NotBlank, @NotNull, @Positive etc.
            @Valid
            // Client jo JSON body bhejega, Spring/Jackson usko CreateProductRequest object mein convert karega.
            @RequestBody CreateProductRequest request,
            // Current HTTP request ki information provide karta hai. Is case mein hum iska URI nikalenge.
            HttpServletRequest httpRequest)
    {
        // Service layer ko createProduct() call kar rahe hain.
        // Request DTO service ko pass ho raha hai.
        // Service business logic execute karke ProductResponse return karegi.
        ProductResponse response = productService.createProduct(request);

//        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>(
//                ResponseStatus.SUCCESS,
//                "Product created successfully",
//                response,
//                LocalDateTime.now(),
//                httpRequest.getRequestURI()
//        );
        // ApiResponse ka Builder use karke standard API response bana rahe hain.
        // <ProductResponse> batata hai ki response ke data mein ProductResponse hoga.
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                // Response ka status SUCCESS set kar rahe hain.
                .status(ResponseStatus.SUCCESS)
                // Client ko readable success message milega.
                .message("Product created successfully")
                // Service se mila ProductResponse actual response data hai.
                .data(response)
                // Current date aur time set kar rahe hain.
                .timestamp(LocalDateTime.now())
                // Current request ka URL path set kar rahe hain.
                // Example: /api/v1/products
                .path(httpRequest.getRequestURI())
                // Builder complete karke ApiResponse object create kar rahe hain.
                .build();

        // HTTP status 201 CREATED ke saath response return hoga.
        // body(apiResponse) mein hamara standard API response jayega.
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // HTTP PUT request handle karega.
    // Complete endpoint: PUT /api/v1/products/{productId}
    @PutMapping("/{productId}")
    // Existing Product update karne ke liye use hoga.
    public ResponseEntity<ApiResponse<ProductResponse>> updateteProduct(
            // URL se productId ki value extract karega.
            // Example: /api/v1/products/10 -> Yahan productId = 10 hoga.
            @PathVariable Long productId,
            // Request body ke JSON ko UpdateProductRequest DTO mein convert karega.
            // @Valid DTO ke validation rules check karega.
            @RequestBody UpdateProductRequest request,
            // Current request ka URI lene ke liye.
            HttpServletRequest httpRequest)
    {
        // Service ko productId aur update request pass kar rahe hain.
        // Service product find karegi, update karegi aur response return karegi.
        ProductResponse response = productService.updateteProduct(productId, request);

        // Standard API response create kar rahe hain.
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .status(ResponseStatus.SUCCESS)
                .message("Product updated successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .path(httpRequest.getRequestURI())
                .build();
        // HTTP 200 OK ke saath updated product return hoga.
        return ResponseEntity.ok(apiResponse);
    }

    // HTTP GET request handle karega.
    // Complete endpoint: GET /api/v1/products/{productId}
    @GetMapping("/{productId}")
    // Ek particular Product fetch karne ke liye.
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            // URL se productId extract karega.
            @PathVariable Long productId,
            // Current request ka URI provide karega.
            HttpServletRequest httpRequest)
    {
        // Service layer se particular product fetch kar rahe hain.
        ProductResponse response = productService.getProductById(productId);

        // Fetched product ke liye standard API response create kar rahe hain.
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .status(ResponseStatus.SUCCESS)
                .message("Product fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .path(httpRequest.getRequestURI())
                .build();
        // HTTP 200 OK ke saath product return hoga.
        return ResponseEntity.ok(apiResponse);
    }

    // HTTP GET request handle karega.
    // Complete endpoint: GET /api/v1/products
    @GetMapping
    // Saare Products fetch karne ke liye.
    // Yahan response mein ProductResponse ki List return hogi.
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProduct(
            // Current request ka URI lene ke liye
            HttpServletRequest httpRequest)
    {
        // Service layer se saare products ki list fetch kar rahe hain.
        List<ProductResponse> response = productService.getAllProduct();
        // List<ProductResponse> ko ApiResponse ke data mein wrap kar rahe hain.
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .status(ResponseStatus.SUCCESS)
                .message("Products fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .path(httpRequest.getRequestURI())
                .build();
        // HTTP 200 OK ke saath products ki list return hogi.
        return ResponseEntity.ok(apiResponse);
    }

    // HTTP GET request handle karega.
    // Complete endpoint: GET /api/v1/products/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    // Particular Category ke saare Products fetch karne ke liye.
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductByCategory(
            // URL se categoryId extract karega.
            // Example: /api/v1/products/category/5 -> Yahan categoryId = 5 hoga.
            @PathVariable Long categoryId,
            // Current request ka URI lene ke liye.
            HttpServletRequest httpRequest)
    {
        // Service layer ko categoryId pass kar rahe hain.
        // Service us category ke products fetch karke list return karegi.
        List<ProductResponse> response = productService.getProductByCategory(categoryId);
        // Products ki list ko standard ApiResponse mein wrap kar rahe hain.
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .status(ResponseStatus.SUCCESS)
                .message("Products fetched successfully for category")
                .data(response)
                .timestamp(LocalDateTime.now())
                .path(httpRequest.getRequestURI())
                .build();
        // HTTP 200 OK ke saath category ke products return honge.
        return ResponseEntity.ok(apiResponse);

    }


}
