package com.ecommerce.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 *  yeh class User Table ko Represent karti hai.
 **/
@Entity // @Entity: Hibernate ko bolta hai: "Ye class database table represent karti hai."Startup ke time Hibernate ise scan karega.
@Table(name = "users")  // Default table name user ho sakta tha.Hum explicitly users use kar rahe hain. Production me explicit naming better hoti hai.
@Getter // Automatically getter methods.
@Setter // Automatically setter methods.
@NoArgsConstructor  // Hibernate ko empty constructor chahiye hota hai.
@AllArgsConstructor // Testing aur object creation me useful.
@Builder    // Readable object creation.
public class User {

    // Primary Key
    @Id // Primary Key: Har row ko uniquely identify karega.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    // User ka full name
//    @Column(nullable = false, length = 100)
//    private String name;

    // Email Unique hona chahiye
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // BCrypt encrypted password
    @Column(nullable = false)
    private String password;

    // User Role(ye Enum se aayega).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Account check: Active ahai ya nahi.
    @Builder.Default    // Builder bhi default value use karega. agar nai kiya to Builder is value ko ignore kar sakta hai.
    @Column(nullable = false)
    private Boolean active = true;

    // Record create hone ka timing.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Last Update Time.
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist()
    {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate()
    {
        this.updatedAt = LocalDateTime.now();
    }
    /*
    * Auditing Fields:
        Abhi hum manually:
        @PrePersist
        @PreUpdate
        use kar rahe hain.Future chapter me Spring Data Auditing padhenge.
        Usme: @CreatedDate, @LastModifiedDate
        Automatically values set hongi. Lekin lifecycle callback samajhne ke liye manual approach best hai.
    */
}

/*
** Validation Annotations:
Question: Entity me validation lagaye ya DTO me?
Production Best Practice:
Request validation → DTO me.
Database constraints → Entity me.

Example:
DTO:
@NotBlank
@Email
private String email;

Entity:
@Column(nullable = false, unique = true)
private String email;

Dono alag responsibility hain.

Isliye abhi Entity me Bean Validation annotations nahi laga rahe. Registration DTO banate waqt @NotBlank, @Email, @Size use karenge.
**/
