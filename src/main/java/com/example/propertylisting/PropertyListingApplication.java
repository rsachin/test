package com.example.propertylisting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PropertyListingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertyListingApplication.class, args);
    }
}
