package ru.jmdevelop.besshoptgbot.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findByCategory(String category);
    List<Product> findByNameContaining(String name);
    List<Product> findAvailableProducts();
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    Product save(Product product);
    void deleteById(Long id);

    // Бизнес-методы
    int countByCategory(String category);
    List<Product> findTopRatedProducts(int limit);
    List<Product> findRecentlyAddedProducts(int days);
}