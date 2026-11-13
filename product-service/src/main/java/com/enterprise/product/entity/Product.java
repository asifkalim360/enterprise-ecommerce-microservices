package com.enterprise.product.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_products_sku",
                        columnNames = "sku")
        },
        indexes = {
                @Index(
                        name = "idx_products_category_id",
                        columnList = "category_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, length = 100)
    private String sku;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    // @ManyToOne    -	Relationship define karta hai
    // FetchType.LAZY -	Related data ko zaroorat par load karne ka behavior
    // optional = false	- Relationship mandatory hai
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn( // @JoinColumn	- Foreign-key column configure karta hai
            name = "category_id",
            nullable = false,   // nullable = false	- Column mein NULL allowed nahi
            foreignKey = @ForeignKey(name = "fk_products_category") // @ForeignKey  -	Database foreign key constraint ka naam/configuration
    )
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private  EntityStatus status = EntityStatus.ACTIVE;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate()
    {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }
}
