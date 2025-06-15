package com.example.propertylisting.util;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.model.Property;
import com.example.propertylisting.model.Property.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataFactory {

    public static final String TEST_TITLE = "Beautiful Family Home";
    public static final String TEST_DESCRIPTION = "A beautiful family home in a quiet neighborhood";
    public static final BigDecimal TEST_PRICE = new BigDecimal("350000.00");
    public static final String TEST_ADDRESS = "123 Main St";
    public static final String TEST_CITY = "Springfield";
    public static final String TEST_COUNTRY = "USA";
    public static final int TEST_BEDROOMS = 3;
    public static final int TEST_BATHROOMS = 2;
    public static final double TEST_SQUARE_FOOTAGE = 1800.0;
    public static final PropertyType TEST_PROPERTY_TYPE = PropertyType.HOUSE;

    public static Property createTestProperty() {
        return createProperty(
                TEST_TITLE,
                TEST_DESCRIPTION,
                TEST_PRICE,
                TEST_ADDRESS,
                TEST_CITY,
                TEST_COUNTRY,
                TEST_BEDROOMS,
                TEST_BATHROOMS,
                TEST_SQUARE_FOOTAGE,
                TEST_PROPERTY_TYPE
        );
    }

    public static Property createTestPropertyWithCity(String city) {
        return createProperty(
                "Property in " + city,
                "A property located in " + city,
                new BigDecimal("300000.00"),
                "123 " + city + " St",
                city,
                "USA",
                3,
                2,
                1800.0,
                PropertyType.HOUSE
        );
    }

    public static Property createTestPropertyWithPrice(BigDecimal price) {
        return createProperty(
                "Property with price " + price,
                "A property with a specific price",
                price,
                "123 Price St",
                "Springfield",
                "USA",
                3,
                2,
                1800.0,
                PropertyType.HOUSE
        );
    }

    public static Property createTestPropertyWithType(PropertyType type) {
        return createProperty(
                type + " Property",
                "A " + type + " type property",
                new BigDecimal("300000.00"),
                "123 " + type + " St",
                "Springfield",
                "USA",
                3,
                2,
                1800.0,
                type
        );
    }

    private static Property createProperty(
            String title,
            String description,
            BigDecimal price,
            String address,
            String city,
            String country,
            int bedrooms,
            int bathrooms,
            double squareFootage,
            PropertyType type
    ) {
        return Property.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(description)
                .price(price)
                .address(address)
                .city(city)
                .country(country)
                .bedrooms(bedrooms)
                .bathrooms(bathrooms)
                .area(squareFootage)
                .type(type)
                .build();
    }

    public static PropertyRequest createTestPropertyRequest() {
        return new PropertyRequest(
                TEST_TITLE,
                TEST_DESCRIPTION,
                TEST_PRICE,
                TEST_ADDRESS,
                TEST_CITY,
                TEST_COUNTRY,
                TEST_BEDROOMS,
                TEST_BATHROOMS,
                TEST_SQUARE_FOOTAGE,
                TEST_PROPERTY_TYPE
        );
    }

    public static PropertyRequest createInvalidPropertyRequest() {
        return new PropertyRequest(
                "", // Invalid: empty title
                "", // Invalid: empty description
                BigDecimal.ZERO, // Invalid: price must be greater than 0
                "", // Invalid: empty address
                "", // Invalid: empty city
                "", // Invalid: empty country
                -1, // Invalid: negative bedrooms
                -1, // Invalid: negative bathrooms
                -1.0, // Invalid: negative square footage
                null // Invalid: null property type
        );
    }

    public static PropertyResponse createTestPropertyResponse() {
        return new PropertyResponse(
                UUID.randomUUID(),
                TEST_TITLE,
                TEST_DESCRIPTION,
                TEST_PRICE,
                TEST_ADDRESS,
                TEST_CITY,
                TEST_COUNTRY,
                TEST_BEDROOMS,
                TEST_BATHROOMS,
                TEST_SQUARE_FOOTAGE,
                TEST_PROPERTY_TYPE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
    }

    public static PropertyRequest createUpdatePropertyRequest() {
        return new PropertyRequest(
                "Updated " + TEST_TITLE,
                "Updated " + TEST_DESCRIPTION,
                TEST_PRICE.add(new BigDecimal("50000.00")),
                "Updated " + TEST_ADDRESS,
                "Updated " + TEST_CITY,
                TEST_COUNTRY,
                TEST_BEDROOMS + 1,
                TEST_BATHROOMS + 1,
                TEST_SQUARE_FOOTAGE + 200.0,
                PropertyType.APARTMENT
        );
    }
}
