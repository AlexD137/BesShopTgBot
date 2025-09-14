package ru.jmdevelop.besshoptgbot.domain.repo.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long  productId);

    Page<Product> findAllByCategoryName(String categoryName, Pageable pageable);

}