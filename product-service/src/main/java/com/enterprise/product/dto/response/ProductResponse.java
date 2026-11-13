package com.enterprise.product.dto.response;

import com.enterprise.product.entity.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductResponse
{
    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
