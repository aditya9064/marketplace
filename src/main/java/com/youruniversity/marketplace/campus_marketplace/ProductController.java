package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
@Component
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    static {
        log.info("Loading ProductController class");
    }

    @Autowired
    private org.springframework.context.ApplicationContext applicationContext;

    @PostConstruct
    private void init() {
        log.info("ProductController initialized with bean name: {}", this.getClass().getSimpleName());
        log.info("Controller instance: {}", this);
        log.info("Application Context: {}", applicationContext);
    }

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.debug("Fetching all available products");
        List<Product> products = productRepository.findByAvailableTrue();
        List<ProductResponse> responses = products.stream()
            .map(this::convertToResponse)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.debug("Fetching product with id: {}", id);
        return productRepository.findById(id)
            .map(this::convertToResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute CreateProductRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Creating new product: {}", request.getTitle());

        if (request.getImages() == null || request.getImages().length == 0) {
            return ResponseEntity.badRequest().body("At least one image is required");
        }

        try {
            User seller = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            Product product = new Product();
            product.setTitle(request.getTitle());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice().doubleValue());
            product.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
            product.setCondition(Condition.valueOf(request.getCondition().toUpperCase()));
            product.setLocation(request.getLocation());
            product.setContactPreference(request.getContactPreference());
            product.setCreatedDate(LocalDateTime.now());
            product.setAvailable(true);
            product.setSeller(seller);

            // Save product first to get ID
            Product savedProduct = productRepository.save(product);

            // Process and save images
            for (int i = 0; i < request.getImages().length; i++) {
                MultipartFile file = request.getImages()[i];
                String filename = imageService.storeImage(file);
                
                ProductImage image = new ProductImage();
                image.setProduct(savedProduct);
                image.setFileName(filename);
                image.setContentType(file.getContentType());
                image.setImageUrl("/api/images/" + filename);
                image.setPrimary(i == 0); // First image is primary
                savedProduct.getImages().add(image);
            }

            // Save product again with images
            savedProduct = productRepository.save(savedProduct);
            return ResponseEntity.ok(convertToResponse(savedProduct));
        } catch (IOException e) {
            log.error("Error processing image upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing image upload");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute CreateProductRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Updating product with id: {}", id);
        
        return productRepository.findById(id)
            .map(product -> {
                User seller = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

                if (!product.getSeller().getId().equals(seller.getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                try {
                    if (request.getTitle() != null) product.setTitle(request.getTitle());
                    if (request.getDescription() != null) product.setDescription(request.getDescription());
                    if (request.getPrice() != null) product.setPrice(request.getPrice().doubleValue());
                    if (request.getCategory() != null) product.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
                    if (request.getCondition() != null) product.setCondition(Condition.valueOf(request.getCondition().toUpperCase()));
                    if (request.getLocation() != null) product.setLocation(request.getLocation());
                    if (request.getContactPreference() != null) product.setContactPreference(request.getContactPreference());

                    // Handle new images if provided
                    if (request.getImages() != null && request.getImages().length > 0) {
                        // Remove old images
                        for (ProductImage oldImage : product.getImages()) {
                            imageService.deleteImage(oldImage.getFileName());
                        }
                        product.getImages().clear();

                        // Add new images
                        for (int i = 0; i < request.getImages().length; i++) {
                            MultipartFile file = request.getImages()[i];
                            String filename = imageService.storeImage(file);
                            
                            ProductImage image = new ProductImage();
                            image.setProduct(product);
                            image.setFileName(filename);
                            image.setContentType(file.getContentType());
                            image.setImageUrl("/api/images/" + filename);
                            image.setPrimary(i == 0);
                            product.getImages().add(image);
                        }
                    }

                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(convertToResponse(updatedProduct));
                } catch (IOException e) {
                    log.error("Error processing image upload", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error processing image upload");
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Deleting product with id: {}", id);
        
        return productRepository.findById(id)
            .map(product -> {
                User seller = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

                if (!product.getSeller().getId().equals(seller.getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                try {
                    // Delete all associated images
                    for (ProductImage image : product.getImages()) {
                        imageService.deleteImage(image.getFileName());
                    }

                    productRepository.delete(product);
                    return ResponseEntity.ok().build();
                } catch (IOException e) {
                    log.error("Error deleting product images", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error deleting product images");
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory().name());
        response.setCondition(product.getCondition().name());
        response.setLocation(product.getLocation());
        response.setContactPreference(product.getContactPreference());
        response.setCreatedDate(product.getCreatedDate());
        response.setAvailable(product.isAvailable());

        // Convert seller information
        ProductResponse.UserDTO sellerDto = new ProductResponse.UserDTO();
        sellerDto.setId(product.getSeller().getId());
        sellerDto.setUsername(product.getSeller().getUsername());
        sellerDto.setName(product.getSeller().getName());
        sellerDto.setEmail(product.getSeller().getEmail());
        response.setSeller(sellerDto);

        // Convert image URLs
        List<String> imageUrls = product.getImages().stream()
            .map(ProductImage::getImageUrl)
            .collect(Collectors.toList());
        response.setImageUrls(imageUrls);

        return response;
    }
}
