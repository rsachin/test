package com.example.propertylisting.dto;

import com.example.propertylisting.model.Property.PropertyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PropertyRequest(
    @Schema(description = "Property title", example = "Modern Apartment in Downtown")
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    String title,

    @Schema(description = "Detailed property description", example = "A beautiful apartment with great view")
    @NotBlank(message = "Description is required")
    String description,

    @Schema(description = "Price of the property", example = "250000.00")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    BigDecimal price,

    @Schema(description = "Full address of the property", example = "123 Main St")
    @NotBlank(message = "Address is required")
    String address,

    @Schema(description = "City where the property is located", example = "New York")
    @NotBlank(message = "City is required")
    String city,

    @Schema(description = "Country where the property is located", example = "USA")
    @NotBlank(message = "Country is required")
    String country,

    @Schema(description = "Number of bedrooms", example = "2")
    @Positive(message = "Bedrooms must be positive")
    Integer bedrooms,

    @Schema(description = "Number of bathrooms", example = "2")
    @Positive(message = "Bathrooms must be positive")
    Integer bathrooms,

    @Schema(description = "Area in square meters", example = "85.5")
    @PositiveOrZero(message = "Area must be positive or zero")
    Double area,

    @Schema(description = "Type of the property", example = "APARTMENT")
    @NotNull(message = "Property type is required")
    PropertyType type
) {}
