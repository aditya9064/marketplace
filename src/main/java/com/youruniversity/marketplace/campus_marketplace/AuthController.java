package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping({"/signup", "/register"})
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Generate username from email if not provided
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            String username = user.getEmail().split("@")[0];
            user.setUsername(username);
        }
        
        log.info("Received registration request for user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.warn("Username already exists: {}", user.getUsername());
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", user.getEmail());
            return ResponseEntity.badRequest().body("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default values for new user
        user.setVerified(false);
        user.setEmailVerified(false);
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail());
        
        log.info("Successfully registered user: {}", user.getUsername());
        return ResponseEntity.ok().body(Map.of(
            "token", token,
            "user", savedUser
        ));
    }

    @PostMapping({"/login", "/signin"})
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        
        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        
        // Check if user exists and password matches
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("Login successful for user: {}", user.getUsername());
            // Generate JWT token
            String token = jwtUtils.generateToken(user.getEmail());
            return ResponseEntity.ok().body(Map.of(
                "token", token,
                "user", user
            ));
        }
        
        log.warn("Login failed for email: {}", loginRequest.getEmail());
        return ResponseEntity.badRequest().body("Invalid email or password");
    }
}
