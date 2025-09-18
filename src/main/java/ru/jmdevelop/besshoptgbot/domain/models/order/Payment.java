package ru.jmdevelop.besshoptgbot.domain.models.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Long id;
    private Order order;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime paidAt;

    public enum PaymentMethod {
        CARD, SBP, APPLE_PAY, GOOGLE_PAY, CASH
    }

    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}