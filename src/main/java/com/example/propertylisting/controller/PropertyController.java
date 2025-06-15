package com.example.propertylisting.controller;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.model.Property.PropertyType;
import com.example.propertylisting.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@Tag(name = "Property Management", description = "APIs for managing property listings")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    @Operation(summary = "Get all properties with filtering and pagination")
    public Page<PropertyResponse> getAllProperties(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable,
            @Parameter(description = "Filter by city") @RequestParam(required = false) String city,
            @Parameter(description = "Minimum price") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Filter by property type") @RequestParam(required = false) String type) {
        PropertyType propertyType = null;
        if (type != null && !type.isBlank()) {
            try {
                propertyType = PropertyType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignoring invalid property type
            }
        }
        return propertyService.getAllProperties(pageable, city, minPrice, maxPrice, propertyType);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a property by ID")
    public PropertyResponse getPropertyById(
            @Parameter(description = "ID of the property to be obtained", required = true)
            @PathVariable UUID id) {
        return propertyService.getPropertyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new property")
    public PropertyResponse createProperty(
            @Valid @RequestBody PropertyRequest request) {
        return propertyService.createProperty(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing property")
    public PropertyResponse updateProperty(
            @Parameter(description = "ID of the property to be updated", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody PropertyRequest request) {
        return propertyService.updateProperty(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a property")
    public void deleteProperty(
            @Parameter(description = "ID of the property to be deleted", required = true)
            @PathVariable UUID id) {
        propertyService.deleteProperty(id);
    }
}
