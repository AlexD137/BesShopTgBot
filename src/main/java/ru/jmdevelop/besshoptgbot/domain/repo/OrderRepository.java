package ru.jmdevelop.besshoptgbot.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.domain.models.order.OrderStatus;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByDateRange(LocalDateTime start, LocalDateTime end);
    Order save(Order order);
    void deleteById(Long id);

    // Бизнес-методы
    List<Order> findRecentOrders(int hours);
    BigDecimal getTotalRevenue();
    Map<OrderStatus, Long> getOrdersCountByStatus();
}