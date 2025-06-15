package com.example.propertylisting.service;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.exception.ResourceNotFoundException;
import com.example.propertylisting.mapper.PropertyMapper;
import com.example.propertylisting.model.Property;
import com.example.propertylisting.model.Property.PropertyType;
import com.example.propertylisting.model.PropertySpecifications;
import com.example.propertylisting.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    @Transactional(readOnly = true)
    public Page<PropertyResponse> getAllProperties(Pageable pageable, String city, Double minPrice, Double maxPrice, PropertyType type) {

        Specification<Property> spec = Specification.where(null);

        if (city != null && !city.isBlank()) {
            spec = spec.and(PropertySpecifications.hasCity(city));
        }

        if (minPrice != null) {
            spec = spec.and(PropertySpecifications.priceGreaterThanOrEqual(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(PropertySpecifications.priceLessThanOrEqual(maxPrice));
        }

        if (type != null) {
            spec = spec.and(PropertySpecifications.hasType(type));
        }

        return propertyRepository.findAll(spec, pageable)
                .map(propertyMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(UUID id) {
        return propertyRepository.findById(id)
                .map(propertyMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    }

    @Transactional
    public PropertyResponse createProperty(PropertyRequest request) {
        Property property = propertyMapper.toEntity(request);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }

    @Transactional
    public PropertyResponse updateProperty(UUID id, PropertyRequest request) {
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        
        propertyMapper.updateEntity(request, existingProperty);
        Property updatedProperty = propertyRepository.save(existingProperty);
        
        return propertyMapper.toResponse(updatedProperty);
    }

    @Transactional
    public void deleteProperty(UUID id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property not found with id: " + id);
        }
        propertyRepository.deleteById(id);
    }
}
