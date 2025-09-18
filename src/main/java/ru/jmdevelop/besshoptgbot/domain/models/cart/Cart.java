package ru.jmdevelop.besshoptgbot.domain.models.cart;

import ru.jmdevelop.besshoptgbot.domain.models.user.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private Long id;
    private User user;
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalPrice;
}
