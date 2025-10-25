package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Long> {
    List<UserFavorites> findByUser(User user);
    List<UserFavorites> findByProduct(Product product);
    Optional<UserFavorites> findByUserAndProduct(User user, Product product);
    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
}
