package ru.jmdevelop.besshoptgbot.domain.models;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum DeliveryType {
    STANDARD("Стандартная доставка", new BigDecimal("350")),
    EXPRESS("Экспресс доставка", new BigDecimal("550")),
    PICKUP("Самовывоз", BigDecimal.ZERO);

    private final String description;
    private final BigDecimal cost;

    DeliveryType(String description, BigDecimal cost) {
        this.description = description;
        this.cost = cost;
    }
}
