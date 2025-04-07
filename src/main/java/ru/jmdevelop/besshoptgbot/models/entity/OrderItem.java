package ru.jmdevelop.besshoptgbot.models.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.jmdevelop.besshoptgbot.models.dom.CartItem;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "orders_items")
@Getter
@Setter
@ToString(exclude = "order")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_items_seq")
    @SequenceGenerator(name = "orders_items_seq", sequenceName = "orders_items_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Long productPrice;

    public static OrderItem from(CartItem cartItem) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .productName(cartItem.getProduct().getName())
                .productPrice(cartItem.getProduct().getPrice())
                .build();
    }
}