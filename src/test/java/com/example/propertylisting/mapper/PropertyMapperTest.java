package com.example.propertylisting.mapper;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.model.Property;
import com.example.propertylisting.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyMapperTest {

    private PropertyMapper propertyMapper;

    @BeforeEach
    void setUp() {
        propertyMapper = Mappers.getMapper(PropertyMapper.class);
    }

    @Test
    void toEntity_ShouldMapPropertyRequestToProperty() {
        // Given
        PropertyRequest request = TestDataFactory.createTestPropertyRequest();

        // When
        Property property = propertyMapper.toEntity(request);

        // Then
        assertThat(property).isNotNull();
        assertThat(property.getTitle()).isEqualTo(request.title());
        assertThat(property.getDescription()).isEqualTo(request.description());
        assertThat(property.getPrice()).isEqualByComparingTo(request.price());
        assertThat(property.getCity()).isEqualTo(request.city());
        assertThat(property.getCountry()).isEqualTo(request.country());
        assertThat(property.getBedrooms()).isEqualTo(request.bedrooms());
        assertThat(property.getBathrooms()).isEqualTo(request.bathrooms());
        assertThat(property.getArea()).isEqualTo(request.area());
        assertThat(property.getType()).isEqualTo(request.type());
    }

    @Test
    void toResponse_ShouldMapPropertyToPropertyResponse() {
        // Given
        Property property = TestDataFactory.createTestProperty();

        // When
        PropertyResponse response = propertyMapper.toResponse(property);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(property.getId());
        assertThat(response.title()).isEqualTo(property.getTitle());
        assertThat(response.description()).isEqualTo(property.getDescription());
        assertThat(response.price()).isEqualByComparingTo(property.getPrice());
        assertThat(response.city()).isEqualTo(property.getCity());
        assertThat(response.country()).isEqualTo(property.getCountry());
        assertThat(response.bedrooms()).isEqualTo(property.getBedrooms());
        assertThat(response.bathrooms()).isEqualTo(property.getBathrooms());
        assertThat(response.area()).isEqualTo(property.getArea());
        assertThat(response.type()).isEqualTo(property.getType());
        assertThat(response.createdAt()).isEqualTo(property.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(property.getUpdatedAt());
        assertThat(response.active()).isEqualTo(property.isActive());
    }

    @Test
    void updateEntity_ShouldUpdatePropertyFromRequest() {
        // Given
        Property property = TestDataFactory.createTestProperty();
        PropertyRequest updateRequest = new PropertyRequest(
                "Updated Title",
                "Updated description",
                new BigDecimal("400000.00"),
                "456 New St",
                "Los Angeles",
                "USA",
                3,
                2,
                100.0,
                property.getType()
        );

        // When
        propertyMapper.updateEntity(updateRequest, property);


        // Then
        assertThat(property.getTitle()).isEqualTo(updateRequest.title());
        assertThat(property.getDescription()).isEqualTo(updateRequest.description());
        assertThat(property.getPrice()).isEqualByComparingTo(updateRequest.price());
        assertThat(property.getAddress()).isEqualTo(updateRequest.address());
        assertThat(property.getCity()).isEqualTo(updateRequest.city());
        assertThat(property.getBedrooms()).isEqualTo(updateRequest.bedrooms());
        assertThat(property.getBathrooms()).isEqualTo(updateRequest.bathrooms());
        assertThat(property.getArea()).isEqualTo(updateRequest.area());
    }
}
