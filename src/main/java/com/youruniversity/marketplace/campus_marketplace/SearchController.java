package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Condition condition,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<Product> products = productRepository.searchProducts(
            keyword, category, condition, location, minPrice, maxPrice);

        return ResponseEntity.ok(
            products.stream()
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
