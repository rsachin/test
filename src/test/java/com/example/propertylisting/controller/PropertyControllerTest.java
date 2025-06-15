package com.example.propertylisting.controller;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.model.Property;
import com.example.propertylisting.service.PropertyService;
import com.example.propertylisting.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static com.example.propertylisting.model.Property.PropertyType.APARTMENT;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
@Disabled
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private JwtService jwtService;

    private Property testProperty;
    private PropertyResponse testPropertyResponse;
    private PropertyRequest testPropertyRequest;
    private String jwtToken;

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

        // Mock JWT token for authenticated requests
        when(jwtService.generateToken("user@example.com")).thenReturn("test-jwt-token");
        jwtToken = "Bearer test-jwt-token";
    }

    @Test
    void createProperty_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPropertyRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createProperty_WithAuth_ShouldCreateProperty() throws Exception {
        when(propertyService.createProperty(any(PropertyRequest.class))).thenReturn(testPropertyResponse);

        mockMvc.perform(post("/api/v1/properties")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPropertyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(testPropertyRequest.title()))
                .andExpect(jsonPath("$.description").value(testPropertyRequest.description()))
                .andExpect(jsonPath("$.price").value(testPropertyRequest.price().doubleValue()))
                .andExpect(jsonPath("$.city").value(testPropertyRequest.city()));
    }

    @Test
    void getPropertyById_ShouldReturnProperty() throws Exception {
        when(propertyService.getPropertyById(testProperty.getId())).thenReturn(testPropertyResponse);

        mockMvc.perform(get("/api/v1/properties/" + testProperty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProperty.getId().toString()))
                .andExpect(jsonPath("$.title").value(testProperty.getTitle()));
    }

    @Test
    void getAllProperties_ShouldReturnPageOfProperties() throws Exception {
        Page<PropertyResponse> propertyPage = new PageImpl<>(Collections.singletonList(testPropertyResponse));
        when(propertyService.getAllProperties(any(Pageable.class),
                any(), any(), any(), any()))
                .thenReturn(propertyPage);

        mockMvc.perform(get("/api/v1/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value(testProperty.getTitle()));
    }

    @Test
    void updateProperty_ShouldReturnUpdatedProperty() throws Exception {
        when(propertyService.updateProperty(any(UUID.class), any(PropertyRequest.class)))
                .thenReturn(testPropertyResponse);

        mockMvc.perform(put("/api/v1/properties/" + testProperty.getId())
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPropertyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProperty.getId().toString()))
                .andExpect(jsonPath("$.title").value(testPropertyRequest.title()));
    }

    @Test
    void deleteProperty_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(propertyService).deleteProperty(testProperty.getId());

        mockMvc.perform(delete("/api/v1/properties/" + testProperty.getId())
                        .header("Authorization", jwtToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProperty_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        PropertyRequest invalidRequest = new PropertyRequest(
                "", // invalid: empty title
                "", // invalid: empty description
                new BigDecimal("-100"), // invalid: negative price
                "", // invalid: empty address
                "", // invalid: empty city
                "", // invalid: empty country
                -1, // invalid: negative bedrooms
                -1, // invalid: negative bathrooms
                -1.0, // invalid: negative area
                null // invalid: null type
        );

        mockMvc.perform(post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed")));
    }
}
