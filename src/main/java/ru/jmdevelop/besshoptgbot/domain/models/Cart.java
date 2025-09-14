package ru.jmdevelop.besshoptgbot.domain.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private Long id;
    private User user;
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalPrice;
}
