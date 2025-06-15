package com.example.propertylisting.config;

import com.example.propertylisting.model.User;
import com.example.propertylisting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void initialize() {
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = User.builder()
                    .id(1L)
                    .email("user@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .enabled(true)
                    .build();
            userRepository.save(user);
        }
    }
}
