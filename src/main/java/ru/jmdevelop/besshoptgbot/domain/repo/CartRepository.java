package ru.jmdevelop.besshoptgbot.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Cart;

import java.util.Optional;

public interface CartRepository {
    Optional<Cart> findById(Long id);
    Optional<Cart> findByUserId(Long userId);
    Cart save(Cart cart);
    void deleteById(Long id);
    void clearCart(Long userId);

    // Специфичные методы
    boolean hasItems(Long userId);
    int getItemsCount(Long userId);
}