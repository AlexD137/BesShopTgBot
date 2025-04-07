package ru.jmdevelop.besshoptgbot.models.dom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@RequiredArgsConstructor
public class ClientOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<CartItem> cartItems;
    private final String clientName;
    private final String phoneNumber;
    private final String city;
    private final String address;

    public long calculateTotalPrice() {
        return cartItems.stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
    }
}