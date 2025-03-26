package ru.jmdevelop.besshoptgbot.models.entity;

import jakarta.persistence.*;
import ru.jmdevelop.besshoptgbot.models.dom.CartItem;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "orders_items")
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_items_seq")
    @SequenceGenerator(name = "orders_items_seq", sequenceName = "orders_items_id_seq", allocationSize = 1)
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

    public OrderItem() {
    }

    public static OrderItem from(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setProductName(cartItem.getProduct().getName());
        orderItem.setProductPrice(cartItem.getProduct().getPrice());
        return orderItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Long getTotalPrice() {
        return quantity * productPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) &&
                Objects.equals(order, orderItem.order) &&
                Objects.equals(product, orderItem.product) &&
                Objects.equals(quantity, orderItem.quantity) &&
                Objects.equals(productName, orderItem.productName) &&
                Objects.equals(productPrice, orderItem.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, product, quantity, productName, productPrice);
    }

    @Override
    public String toString() {
        return "OrderItem [id=" + id +
                ", order=" + order +
                ", product=" + product +
                ", quantity=" + quantity +
                ", productName=" + productName +
                ", productPrice=" + productPrice + "]";
    }

}