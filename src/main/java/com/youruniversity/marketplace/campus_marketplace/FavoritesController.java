package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {
    
    @Autowired
    private UserFavoritesRepository favoritesRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;

    // Add a product to favorites
    @PostMapping("/{productId}")
    public ResponseEntity<?> addToFavorites(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (favoritesRepository.existsByUserAndProduct(user, product)) {
            return ResponseEntity.badRequest().body("Product already in favorites");
        }
        
        UserFavorites favorite = new UserFavorites();
        favorite.setUser(user);
        favorite.setProduct(product);
        
        favoritesRepository.save(favorite);
        return ResponseEntity.ok().build();
    }

    // Remove a product from favorites
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromFavorites(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        favoritesRepository.deleteByUserAndProduct(user, product);
        return ResponseEntity.ok().build();
    }

    // Get user's favorite products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getFavorites(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(
            favoritesRepository.findByUser(user)
                .stream()
                .map(UserFavorites::getProduct)
                .map(this::convertToResponse)
                .collect(Collectors.toList())
        );
    }

    // Helper method to convert Product to ProductResponse
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory().toString());
        response.setCondition(product.getCondition().toString());
        response.setLocation(product.getLocation());
        response.setContactPreference(product.getContactPreference());
        response.setCreatedDate(product.getCreatedDate());
        response.setAvailable(product.isAvailable());

        ProductResponse.UserDTO sellerDto = new ProductResponse.UserDTO();
        sellerDto.setId(product.getSeller().getId());
        sellerDto.setUsername(product.getSeller().getUsername());
        sellerDto.setName(product.getSeller().getName());
        sellerDto.setEmail(product.getSeller().getEmail());
        response.setSeller(sellerDto);

        return response;
    }
}
