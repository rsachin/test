package com.example.propertylisting.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequest(
        String email,
        String password
) {
}
