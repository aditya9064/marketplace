package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategory(Category category);
    List<Produt> findByAvailableTrue();
    List<Produt> findBySeller(User seller);
    List<Product> findByCategoryAndAvailableTrue(Category category);
    List<Product> findByTitleContainingIgnoreCase(String keyword);
    
}
