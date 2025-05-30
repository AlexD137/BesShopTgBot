package ru.jmdevelop.besshoptgbot.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.models.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}