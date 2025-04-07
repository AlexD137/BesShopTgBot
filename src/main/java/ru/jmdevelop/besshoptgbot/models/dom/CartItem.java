package ru.jmdevelop.besshoptgbot.models.dom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jmdevelop.besshoptgbot.models.entity.Product;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Product product;
    private int quantity;

    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getTotalPrice() {
        return quantity * product.getPrice();
    }
}
