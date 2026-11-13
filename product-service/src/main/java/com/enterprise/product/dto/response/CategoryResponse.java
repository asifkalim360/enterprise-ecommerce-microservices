package com.enterprise.product.dto.response;


import com.enterprise.product.entity.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CategoryResponse
{
    private Long id;
    private String name;
    private String description;
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
