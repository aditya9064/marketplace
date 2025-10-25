package com.youruniversity.marketplace.campus_marketplace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:19006"})
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            logger.info("Registering new user with email: {}", user.getEmail());
            
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                logger.warn("Registration failed - Email already exists: {}", user.getEmail());
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already exists"));
            }
            
            // Set username as email
            user.setUsername(user.getEmail());

            // Encode password
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            logger.debug("Password encoded successfully");
            
            // Save user
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", user.getEmail());
            
            // Generate JWT token
            String token = jwtUtils.generateToken(savedUser.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", savedUser);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during registration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error registering user: " + e.getMessage()));
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(Map.of("message", "Hello from the server!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt starting");
            logger.info("Request body email: {}", loginRequest.getEmail());
            logger.info("Request body password length: {}", 
                loginRequest.getPassword() != null ? loginRequest.getPassword().length() : "null");
            
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            
            if (userOpt.isEmpty()) {
                logger.warn("Login failed - User not found: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
            }

            User user = userOpt.get();
            
            // Log detailed information about the password comparison
            logger.info("Found user in database: {}", user.getEmail());
            logger.info("User password hash in DB: {}", user.getPassword());
            logger.info("Attempting to match passwords...");
            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            logger.info("Password match result: {}", matches);
            
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                logger.warn("Login failed - Invalid password for user: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
            }

            // Generate JWT token
            String token = jwtUtils.generateToken(user.getEmail());
            logger.info("Login successful for user: {}", user.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during login: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error during login: " + e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String email = jwtUtils.getEmailFromToken(token);
            logger.debug("Getting profile for user: {}", email);
            
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                logger.warn("Profile not found for user: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
            }

            logger.info("Profile retrieved successfully for user: {}", email);
            return ResponseEntity.ok(user.get());
        } catch (Exception e) {
            logger.error("Error fetching profile: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error fetching profile: " + e.getMessage()));
        }
    }
}
