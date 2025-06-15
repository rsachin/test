package com.example.propertylisting.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
