package com.example.propertylisting.mapper;

import com.example.propertylisting.dto.PropertyRequest;
import com.example.propertylisting.dto.PropertyResponse;
import com.example.propertylisting.model.Property;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PropertyMapper {

    Property toEntity(PropertyRequest request);

    @Mapping(target = "id", source = "id")
    PropertyResponse toResponse(Property property);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PropertyRequest request, @MappingTarget Property property);
}
