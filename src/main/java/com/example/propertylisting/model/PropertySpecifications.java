package com.example.propertylisting.model;

import org.springframework.data.jpa.domain.Specification;
import com.example.propertylisting.model.Property.PropertyType;

public class PropertySpecifications {
    
    public static Specification<Property> hasCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%");
        };
    }
    
    public static Specification<Property> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }
    
    public static Specification<Property> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
    
    public static Specification<Property> hasType(PropertyType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("type"), type);
        };
    }
}
