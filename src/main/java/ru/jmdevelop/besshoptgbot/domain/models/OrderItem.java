package ru.jmdevelop.besshoptgbot.domain.models;

import ru.jmdevelop.besshoptgbot.domain.models.product.Product;

import java.math.BigDecimal;

public class OrderItem {


    private Long id;
    private Order order;
    private Product product;
    private Integer quantity;
    private BigDecimal priceAtTime;


}
