package com.enterprise.product.service.impl;

import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.enterprise.product.dto.request.CreateProductRequest;
import com.enterprise.product.dto.request.UpdateProductRequest;
import com.enterprise.product.dto.response.ProductResponse;
import com.enterprise.product.entity.Category;
import com.enterprise.product.entity.Product;
import com.enterprise.product.repository.CategoryRepository;
import com.enterprise.product.repository.ProductRepository;
import com.enterprise.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Service: Spring ko batata hai ki ye service-layer class hai.
// Spring iska object (Bean) khud manage karega.
@Service

// @RequiredArgsConstructor: Lombok final fields ke liye constructor banata hai.
// Spring constructor injection ke through repositories provide karega.
@RequiredArgsConstructor

// Is class ke public methods by default transaction ke andar execute honge.
// Database operation fail hone par transaction rollback ho sakta hai.
@Transactional
public class ProductServiceImpl implements ProductService
{
    // Product table ke database operations ke liye.
    private final ProductRepository productRepository;

    // Category table ke database oerations ke liye.
    private final CategoryRepository categoryRepository;

    //----------------CREATE PRODUCT------------------------//
    @Override
    public ProductResponse createProduct(CreateProductRequest request)
    {
        // Step 1: Check kro ki ye SKU pahle se database me exists karta hai ya nahi.
        // trim() extra spaces remove karne ke liye.
        // existsBySku() method Spring Data JPA ka derived query method hai.
        if(productRepository.existsBySku(request.getSku().trim()))
        {
            // Agar SKU already exist karta hai to business exception throw hogi.
            throw new BusinessException("Product already exists with SKU: " + request.getSku());
        }

        // Step 2: Request DTO se categoryId lekar database me category search kar rahe hain.
        // findById() Optional return karta hai.
        // Agar yahan pe Category Nahi mila toh ResourceNotFoundException throw hoga.
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        // Step 3: New Product entity object create karega.
        Product product = new Product();
        // Request DTO se product ke name ko lekar Entity me set ka rahe hain.
        product.setName(request.getName().trim());
        // Request DTO se product ke Description ko lekar Entity me set ka rahe hain.
        product.setDescription(request.getDescription());
        // SKU ko set kar rahe hain bina kisi extra spaces ke.
        product.setSku(request.getSku().trim());
        // Request DTO se product ke price ko lekar Entity me set ka rahe hain.
        product.setPrice(request.getPrice());
        // Product Entity ke sath Category entity ko associate kar rahe hain.
        // Yahan category_id foreign key ke through relationship maintain karti hai.
        product.setCategory(category);

        // Step 4: Product entity ko database me save kar rhe hain.
        // yahan pe Saved Entity return hogi jisme generated ID aur audit field bhi hoga.
        Product savedProduct = productRepository.save(product);

        // Step 5: Product entity ko ProductResponse DTO me convert kar rahe hain.
        // Controller es response ko client tak JSON me return kar dega.
        return mapToResponse(savedProduct);
    }

    //-----------------------UPDATE PRODUCT------------------------------//
    @Override
    public ProductResponse updateteProduct(Long productId, UpdateProductRequest request)
    {
        // Step1: Product ID se exixting product databse me search kar rahe hain.
        // Agar product nahi mila toh exception throw hoga.
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Step 2: Request se new SKU liya hai aur extra spaces remove ka diya hai.
        String newSku = request.getSku().trim();

        // Step 3: Check kar rahe hain ki new SKU kisi dusre product ke pass to nahi hai.
        productRepository.findBySku(newSku)
                // Agar SKU mila hai toh check karo ki uska ID current product ID se alag hai.
                // Apne hi product ka exixting SKU rakhna allowed hai.
                .filter(existingProduct -> !existingProduct.getId().equals(productId))
                // Agar kisi dusre product ke paas sane SKU hai, toh Business exception throw hoga.
                .ifPresent(existingProduct -> {
                    throw new BusinessException("Product already exists with SKU: " + newSku);
                });

        // Step 4: Request me di gae category ID se category fetch kar rahe hain.
        // Agar yahan Category nahi milti hai toh exception throw hoga.
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        // Step 5: Existing Product entity ke fields ko update kar rahe hain.
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setSku(newSku);
        product.setPrice(request.getPrice());
        // Product ki category bhi update/associate kar rhe hain.
        product.setCategory(category);

        // Step 6: Update product database me save kar rahe hain.
        Product updatedProduct = productRepository.save(product);

        // Update Entity ko databse DTo me convert karke return kar rahe hain.
        return mapToResponse(updatedProduct);
    }

    //------------------------------------- GET PRODUCT BY ID -------------------------------------//
    @Override
    // Read-only transaction: es method me database data read karna hota hai. lekin modify nahi kar sakte hain.
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId)
    {
        // ID se product fetch kar rahe hain. agar Product nahi mila to exception throw hoga.
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Product entity ko Response DTO me convert karke return kar rahe hain.
        return mapToResponse(product);
    }


    //-------------------------------- GET ALL PRODUCTS -------------------------------//
    @Override
    @Transactional
    public List<ProductResponse> getAllProduct()
    {
        // database se saare products fetch kar rahe hain.
        return productRepository.findAll()
                // List<Product> ko Stream me convert karenge.
                .stream()
                // fhir Har product entity ko mapToResponse() se ProductResponse me convert karenge.
                .map(this::mapToResponse)
                // Stream ke results ko List<ProductResponse> me collect kar rahe hian.
                .toList();
    }

    //------------------------------ GET PRODUCTS BY CATEGORY ---------------------------//
    @Override
    public List<ProductResponse> getProductByCategory(Long categoryId)
    {
        // Pahle humlog check kar rhe han ki requested category databse me exist karti bhi hai ya nahi.
        if(!categoryRepository.existsById(categoryId))
        {
            // Agar category nahi mili to exception throw karega,
            throw new ResourceNotFoundException("Category bot found with id: " + categoryId);
        }
        // Category ID ke basis pe us category ke saare products fetch karenge.
        return productRepository.findByCategoryId(categoryId)
                //Sabse pahle List<Product> ko Stream me convert karenge.
                .stream()
                // fhir Har product entity ko mapToResponse() se ProductResponse me convert karenge.
                .map(this::mapToResponse)
                // Uske baad Converted results ko List mein return kar denge.
                .toList();
    }

    //-------------------------ENTITY TO DTO CONVERSION--------------------------
    // ye ek private helper method hai.(eske jagah humlog spring ka mapper bhi use kar sakte hain.)
    // Create , update, Get By ID aur GetAll sab me eska use humlog kar rahe hain.
    // eslea production me industry me mappe rka use karenge abhi samjhne ke liye custom method ban liya hain.
    private ProductResponse mapToResponse(Product product)
    {
        // Product entity ke fields ko lekar ProductResponse DTO ka object bana rahe hain..
        return new ProductResponse(
                product.getId(),                    // Product ID
                product.getName(),                  // Product name
                product.getDescription(),           // Product description
                product.getSku(),                   // Product SKU
                product.getPrice(),                 // Product price
                product.getCategory().getId(),      // Associated category ID
                product.getCategory().getName(),    // Associated category name
                product.getStatus(),                // Product status
                product.getCreatedAt(),             // Creation timestamp
                product.getUpdatedAt()              // Last update timestamp
        );
    }
}
