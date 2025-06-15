package com.example.propertylisting.dto;

import com.example.propertylisting.model.Property.PropertyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Property response")
public record PropertyResponse(
    @Schema(description = "Unique identifier of the property", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    
    @Schema(description = "Property title", example = "Modern Apartment in Downtown")
    String title,
    
    @Schema(description = "Detailed property description", example = "A beautiful apartment with great view")
    String description,
    
    @Schema(description = "Price of the property", example = "250000.00")
    BigDecimal price,
    
    @Schema(description = "Full address of the property", example = "123 Main St")
    String address,
    
    @Schema(description = "City where the property is located", example = "New York")
    String city,
    
    @Schema(description = "Country where the property is located", example = "USA")
    String country,
    
    @Schema(description = "Number of bedrooms", example = "2")
    Integer bedrooms,
    
    @Schema(description = "Number of bathrooms", example = "2")
    Integer bathrooms,
    
    @Schema(description = "Area in square meters", example = "85.5")
    Double area,
    
    @Schema(description = "Type of the property", example = "APARTMENT")
    PropertyType type,
    
    @Schema(description = "Date and time when the property was created", example = "2023-01-01T12:00:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Date and time when the property was last updated", example = "2023-01-01T12:00:00")
    LocalDateTime updatedAt,
    
    @Schema(description = "Indicates if the property is active", example = "true")
    boolean active
) {}
