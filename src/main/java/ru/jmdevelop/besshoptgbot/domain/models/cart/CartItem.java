package ru.jmdevelop.besshoptgbot.domain.models.cart;

import ru.jmdevelop.besshoptgbot.domain.models.product.Product;

public class CartItem {
    private Long id;
    private Product product;
    private Integer quantity;
}
