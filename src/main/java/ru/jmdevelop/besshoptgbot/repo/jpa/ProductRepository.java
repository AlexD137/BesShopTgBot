package ru.jmdevelop.besshoptgbot.repo.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.models.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(Long  productId);

    Page<Product> findAllByCategoryName(String categoryName, Pageable pageable);

}