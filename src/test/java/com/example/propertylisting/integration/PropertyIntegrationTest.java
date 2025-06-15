package com.example.propertylisting.integration;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.model.Property;
import com.example.propertylisting.repository.PropertyRepository;
import com.example.propertylisting.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.propertylisting.model.Property.PropertyType.APARTMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class PropertyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyRepository propertyRepository;

    @AfterEach
    void tearDown() {
        propertyRepository.deleteAll();
    }

    @Test
    void createProperty_ShouldSaveAndReturnCreatedProperty() throws Exception {
        // Given
        PropertyRequest request = TestDataFactory.createTestPropertyRequest();

        // When & Then
        mockMvc.perform(post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(request.title()))
                .andExpect(jsonPath("$.description").value(request.description()))
                .andExpect(jsonPath("$.price").value(request.price().doubleValue()))
                .andExpect(jsonPath("$.city").value(request.city()))
                .andExpect(jsonPath("$.country").value(request.country()))
                .andExpect(jsonPath("$.bedrooms").value(request.bedrooms()))
                .andExpect(jsonPath("$.bathrooms").value(request.bathrooms()))
                .andExpect(jsonPath("$.area").value(request.area()))
                .andExpect(jsonPath("$.type").value(APARTMENT.name()));

        // Verify in database
        List<Property> properties = propertyRepository.findAll();
        assertThat(properties).hasSize(1);
        assertThat(properties.get(0).getTitle()).isEqualTo(request.title());
    }

    @Test
    void getProperty_ShouldReturnProperty_WhenExists() throws Exception {
        // Given
        Property savedProperty = propertyRepository.save(TestDataFactory.createTestProperty());

        // When & Then
        mockMvc.perform(get("/api/v1/properties/" + savedProperty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProperty.getId().toString()))
                .andExpect(jsonPath("$.title").value(savedProperty.getTitle()))
                .andExpect(jsonPath("$.description").value(savedProperty.getDescription()));
    }

    @Test
    void getProperty_ShouldReturnNotFound_WhenNotExists() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(get("/api/v1/properties/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Property not found with id: " + nonExistentId));
    }

    @Test
    void getAllProperties_ShouldReturnAllProperties() throws Exception {
        // Given
        Property property1 = TestDataFactory.createTestProperty();
        property1.setTitle("Apartment 1");
        
        Property property2 = TestDataFactory.createTestProperty();
        property2.setTitle("Apartment 2");
        property2.setCity("Los Angeles");
        
        propertyRepository.saveAll(List.of(property1, property2));

        // When & Then
        mockMvc.perform(get("/api/v1/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is(oneOf("Apartment 1", "Apartment 2"))))
                .andExpect(jsonPath("$.content[1].title", is(oneOf("Apartment 1", "Apartment 2"))));
    }

    @Test
    void getAllProperties_ShouldFilterByCity() throws Exception {
        // Given
        Property property1 = TestDataFactory.createTestProperty();
        property1.setTitle("Apartment 1");
        property1.setCity("New York");
        
        Property property2 = TestDataFactory.createTestProperty();
        property2.setTitle("Apartment 2");
        property2.setCity("Los Angeles");
        
        propertyRepository.saveAll(List.of(property1, property2));

        // When & Then
        mockMvc.perform(get("/api/v1/properties?city=New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].city").value("New York"));
    }

    @Test
    void updateProperty_ShouldUpdateAndReturnUpdatedProperty() throws Exception {
        // Given
        Property savedProperty = propertyRepository.save(TestDataFactory.createTestProperty());
        
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
                APARTMENT
        );

        // When & Then
        mockMvc.perform(put("/api/v1/properties/" + savedProperty.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProperty.getId().toString()))
                .andExpect(jsonPath("$.title").value(updateRequest.title()))
                .andExpect(jsonPath("$.description").value(updateRequest.description()))
                .andExpect(jsonPath("$.price").value(updateRequest.price().doubleValue()))
                .andExpect(jsonPath("$.city").value(updateRequest.city()));

        // Verify in database
        Property updatedProperty = propertyRepository.findById(savedProperty.getId()).orElseThrow();
        assertThat(updatedProperty.getTitle()).isEqualTo(updateRequest.title());
        assertThat(updatedProperty.getDescription()).isEqualTo(updateRequest.description());
        assertThat(updatedProperty.getPrice()).isEqualByComparingTo(updateRequest.price());
        assertThat(updatedProperty.getCity()).isEqualTo(updateRequest.city());
    }

    @Test
    void deleteProperty_ShouldDeleteProperty() throws Exception {
        // Given
        Property savedProperty = propertyRepository.save(TestDataFactory.createTestProperty());

        // When & Then
        mockMvc.perform(delete("/api/v1/properties/" + savedProperty.getId()))
                .andExpect(status().isNoContent());

        // Verify in database
        assertThat(propertyRepository.existsById(savedProperty.getId())).isFalse();
    }
}
