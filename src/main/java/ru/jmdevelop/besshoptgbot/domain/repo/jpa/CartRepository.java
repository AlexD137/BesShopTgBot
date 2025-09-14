package ru.jmdevelop.besshoptgbot.domain.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
