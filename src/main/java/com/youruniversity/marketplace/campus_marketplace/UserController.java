package com.youruniversity.marketplace.campus_marketplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserController() {
        log.info("UserController initialized");
    }
    
        @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
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
        User savedUser = userRepository.save(user);
        log.info("Successfully registered user: {}", user.getUsername());
        return ResponseEntity.ok(savedUser);
    }
    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    // Find user by email
    User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
    
    // Check if user exists and password matches
    if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        // Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail());
        return ResponseEntity.ok().body(Map.of("token", token));
    }
    
    return ResponseEntity.badRequest().body("Invalid email or password");
}
    
}
