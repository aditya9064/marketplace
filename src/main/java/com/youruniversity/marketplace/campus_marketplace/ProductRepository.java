package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByAvailableTrue();
    List<Product> findBySeller(User seller);
    List<Product> findByCategoryAndAvailableTrue(Category category);
    List<Product> findByTitleContainingIgnoreCase(String keyword);
    
    // Search by keyword in title or description
    @Query("SELECT p FROM Product p WHERE p.available = true AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
    
    // Filter by price range
    @Query("SELECT p FROM Product p WHERE p.available = true AND " +
           "p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(
        @Param("minPrice") BigDecimal minPrice, 
        @Param("maxPrice") BigDecimal maxPrice
    );
    
    // Combined search with filters
    @Query("SELECT p FROM Product p WHERE p.available = true " +
           "AND (:category IS NULL OR p.category = :category) " +
           "AND (:condition IS NULL OR p.condition = :condition) " +
           "AND (:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:keyword IS NULL OR (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    List<Product> searchProducts(
        @Param("keyword") String keyword,
        @Param("category") Category category,
        @Param("condition") Condition condition,
        @Param("location") String location,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
}
