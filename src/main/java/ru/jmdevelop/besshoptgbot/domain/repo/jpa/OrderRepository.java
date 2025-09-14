package ru.jmdevelop.besshoptgbot.domain.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}