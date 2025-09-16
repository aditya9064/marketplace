package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        log.debug("Fetching all available products");
        List<Product> products = productRepository.findByAvailableTrue();
        List<ProductResponse> responses = products.stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
        log.debug("Fetching product with id: {}", id);
        return productRepository.findById(id)
        .map(this::convertToResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<?> createProduct(
        log.debug("Creating new product: {}", request.getTitle());
        @Valid @RequestBody CreateProductRequest request,
        @AuthenticationPrincipal UserDetails userDetails){
            User seller = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            Product product = new Product();
            product.setTitle(request.getTitle());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setCategory(request.getCategory());
            product.setCondition(request.getCondition());
            product.setLocation(request.getLocation());
            product.setContactPreference(request.getContactPreference());
            product.setCreatedDate(LocalDateTime.now());
            product.setAvailable(true);
            product.setSeller(seller);

            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(convertToResponse(savedProduct));
        }
        @PutMapping("/{id}")
        public ResponseEntity<?> updateProduct(
            log.debug("Updating product with id: {}", id);
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
                return productRepository.findById(id)
                .map(product -> {
                    User seller = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(()-> new RuntimeException("User not found"));

                    if (!product.getSeller().getId().equals(seller.getId())){
                        return ResponseEntity.status(403).build();
                    }
                    if (request.getTitle() != null) product.setTitle(request.getTitle());
                    if (request.getDescription() != null) produdct.setDescription(request.getDescription());
                    if (request.getPrice() != null) product.setPrice(request.getPrice());
                    if (request.getCategory() != null) product.setCategory(request.getCategory());
                    if (request.getCondition() != null) product.setCondotion(request.getCondition());
                    if (reuqest.getLocation() != null) product.setLocation(request.getLocation());
                    if (request.getContactPreference() != null) product.setContactPreference(request.getContactPreference());
                if (request.getAvailable() != null) product.setAvailable(request.getAvailable());

                Product updatedProduct = productRepository.save(product);
                return ResponseEntity.ok(convertToResponse(updatedProduct));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Deleting product with id: {}", id);
        return productRepository.findById(id)
            .map(product -> {
                // Check if the current user is the seller
                User seller = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                if (!product.getSeller().getId().equals(seller.getId())) {
                                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                productRepository.delete(product);
                return ResponseEntity.ok().build();
            }) .orElse(ResponseEntity.notFound().build());
    }

    // Helper method to convert Product to ProductResponse
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setCondition(product.getCondition());
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

