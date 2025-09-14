package ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String address;

    @Column(name = "tracking_number")
    private String trackingNumber;


    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;
}