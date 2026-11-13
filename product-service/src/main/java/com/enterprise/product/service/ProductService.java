package com.enterprise.product.service;

import com.enterprise.product.dto.request.CreateProductRequest;
import com.enterprise.product.dto.request.UpdateProductRequest;
import com.enterprise.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductService
{
    public ProductResponse createProduct(CreateProductRequest request);

    public ProductResponse updateteProduct(Long productId, UpdateProductRequest request);

    public ProductResponse getProductById(Long productId);

    public List<ProductResponse> getAllProduct();

    public List<ProductResponse> getProductByCategory(Long categoryId);

}
//-----------------Methods ki responsibility---------------------
// Method	                    Responsibility
// createProduct()	        -   Naya product create
// updateProduct()	        -   Existing product update
// getProductById()	        -   ID se product fetch
// getAllProducts()	        -   Saare products fetch
// getProductsByCategory()  -	Category ID se products fetch
//----------------------------------------------------------------