package ru.jmdevelop.besshoptgbot.domain.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private Long id;
    private User user;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private DeliveryType deliveryType;
    private String deliveryAddress;
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal totalAmount;

}

