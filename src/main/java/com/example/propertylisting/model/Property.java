package com.example.propertylisting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @Positive(message = "Bedrooms must be positive")
    private Integer bedrooms;

    @Positive(message = "Bathrooms must be positive")
    private Integer bathrooms;

    @PositiveOrZero(message = "Area must be positive or zero")
    private Double area; // in square meters

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Property type is required")
    private PropertyType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean active = true;

    public enum PropertyType {
        APARTMENT, HOUSE, VILLA, LAND, COMMERCIAL, OTHER
    }
}
