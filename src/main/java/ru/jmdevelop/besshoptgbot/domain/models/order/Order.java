package ru.jmdevelop.besshoptgbot.domain.models.order;

import lombok.Data;
import ru.jmdevelop.besshoptgbot.domain.models.product.PromoCode;
import ru.jmdevelop.besshoptgbot.domain.models.user.User;

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
    private PromoCode promoCode;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;

}

