package ru.jmdevelop.besshoptgbot.domain.models;

import java.math.BigDecimal;

public class OrderItem {


    private Long id;
    private Order order;
    private Product product;
    private Integer quantity;
    private BigDecimal priceAtTime;


}
