package com.example.propertylisting.service;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.exception.ResourceNotFoundException;
import com.example.propertylisting.mapper.PropertyMapper;
import com.example.propertylisting.model.Property;
import com.example.propertylisting.model.PropertySpecifications;
import com.example.propertylisting.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.propertylisting.model.Property.PropertyType.APARTMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private PropertyService propertyService;

    private Property testProperty;
    private PropertyResponse testPropertyResponse;
    private PropertyRequest testPropertyRequest;

    @BeforeEach
    void setUp() {
        UUID propertyId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        testProperty = Property.builder()
                .id(propertyId)
                .title("Modern Apartment")
                .description("A beautiful modern apartment")
                .price(new BigDecimal("250000.00"))
                .address("123 Main St")
                .city("New York")
                .country("USA")
                .bedrooms(2)
                .bathrooms(2)
                .area(85.5)
                .type(APARTMENT)
                .createdAt(now)
                .updatedAt(now)
                .active(true)
                .build();

        testPropertyResponse = new PropertyResponse(
                propertyId,
                "Modern Apartment",
                "A beautiful modern apartment",
                new BigDecimal("250000.00"),
                "123 Main St",
                "New York",
                "USA",
                2,
                2,
                85.5,
                APARTMENT,
                now,
                now,
                true
        );

        testPropertyRequest = new PropertyRequest(
                "Modern Apartment",
                "A beautiful modern apartment",
                new BigDecimal("250000.00"),
                "123 Main St",
                "New York",
                "USA",
                2,
                2,
                85.5,
                APARTMENT
        );
    }

    @Test
    void getPropertyById_ShouldReturnProperty_WhenFound() {
        // Arrange
        when(propertyRepository.findById(testProperty.getId())).thenReturn(Optional.of(testProperty));
        when(propertyMapper.toResponse(testProperty)).thenReturn(testPropertyResponse);

        // Act
        PropertyResponse result = propertyService.getPropertyById(testProperty.getId());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testProperty.getId());
        assertThat(result.title()).isEqualTo(testProperty.getTitle());
        
        verify(propertyRepository).findById(testProperty.getId());
        verify(propertyMapper).toResponse(testProperty);
    }

    @Test
    void getPropertyById_ShouldThrowException_WhenNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(propertyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> propertyService.getPropertyById(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Property not found with id: " + nonExistentId);
                
        verify(propertyRepository).findById(nonExistentId);
        verify(propertyMapper, never()).toResponse(any());
    }

    @Test
    void createProperty_ShouldReturnCreatedProperty() {
        // Arrange
        when(propertyMapper.toEntity(testPropertyRequest)).thenReturn(testProperty);
        when(propertyRepository.save(testProperty)).thenReturn(testProperty);
        when(propertyMapper.toResponse(testProperty)).thenReturn(testPropertyResponse);

        // Act
        PropertyResponse result = propertyService.createProperty(testPropertyRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testProperty.getId());
        assertThat(result.title()).isEqualTo(testPropertyRequest.title());
        
        verify(propertyMapper).toEntity(testPropertyRequest);
        verify(propertyRepository).save(testProperty);
        verify(propertyMapper).toResponse(testProperty);
    }

    @Test
    void updateProperty_ShouldReturnUpdatedProperty_WhenFound() {
        // Arrange
        PropertyRequest updateRequest = new PropertyRequest(
                "Updated Title",
                "Updated description",
                new BigDecimal("300000.00"),
                "456 New St",
                "Los Angeles",
                "USA",
                3,
                2,
                100.0,
                APARTMENT
        );

        Property updatedProperty = Property.builder()
                .id(testProperty.getId())
                .title("Updated Title")
                .description("Updated description")
                .price(new BigDecimal("300000.00"))
                .address("456 New St")
                .city("Los Angeles")
                .country("USA")
                .bedrooms(3)
                .bathrooms(2)
                .area(100.0)
                .type(APARTMENT)
                .createdAt(testProperty.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();

        PropertyResponse updatedResponse = new PropertyResponse(
                updatedProperty.getId(),
                "Updated Title",
                "Updated description",
                new BigDecimal("300000.00"),
                "456 New St",
                "Los Angeles",
                "USA",
                3,
                2,
                100.0,
                APARTMENT,
                updatedProperty.getCreatedAt(),
                updatedProperty.getUpdatedAt(),
                true
        );

        when(propertyRepository.findById(testProperty.getId())).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(updatedProperty);
        when(propertyMapper.toResponse(updatedProperty)).thenReturn(updatedResponse);

        // Act
        PropertyResponse result = propertyService.updateProperty(testProperty.getId(), updateRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testProperty.getId());
        assertThat(result.title()).isEqualTo("Updated Title");
        assertThat(result.description()).isEqualTo("Updated description");
        assertThat(result.price()).isEqualByComparingTo("300000.00");
        
        verify(propertyRepository).findById(testProperty.getId());
        verify(propertyMapper).updateEntity(eq(updateRequest), eq(testProperty));
        verify(propertyRepository).save(testProperty);
        verify(propertyMapper).toResponse(updatedProperty);
    }

    @Test
    void deleteProperty_ShouldDeleteProperty_WhenFound() {
        // Arrange
        when(propertyRepository.existsById(testProperty.getId())).thenReturn(true);

        // Act
        propertyService.deleteProperty(testProperty.getId());

        // Assert
        verify(propertyRepository).existsById(testProperty.getId());
        verify(propertyRepository).deleteById(testProperty.getId());
    }

    @Test
    void deleteProperty_ShouldThrowException_WhenNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(propertyRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> propertyService.deleteProperty(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Property not found with id: " + nonExistentId);
                
        verify(propertyRepository).existsById(nonExistentId);
        verify(propertyRepository, never()).deleteById(any());
    }

    @Test
    void getAllProperties_ShouldReturnFilteredProperties() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Property> propertyPage = new PageImpl<>(List.of(testProperty), pageable, 1);
        
        when(propertyRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(propertyPage);
        when(propertyMapper.toResponse(testProperty)).thenReturn(testPropertyResponse);

        // Act
        Page<PropertyResponse> result = propertyService.getAllProperties(pageable,
                "New York", 200000.0, 300000.0, APARTMENT);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).city()).isEqualTo("New York");
        
        verify(propertyRepository).findAll(any(Specification.class), eq(pageable));
        verify(propertyMapper).toResponse(testProperty);
    }
}
