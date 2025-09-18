package ru.jmdevelop.besshoptgbot.domain.repo;

import ru.jmdevelop.besshoptgbot.domain.models.order.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findById(Long id);
    Optional<Payment> findByOrderId(Long orderId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    Payment save(Payment payment);

    // Бизнес-методы
    List<Payment> findPendingPayments();
    boolean existsByTransactionId(String transactionId);
}