package ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_requests")
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;


    public enum RequestType {
        CALL, COMPLAINT, FEEDBACK, SUPPORT_EMAIL, DELIVERY_COMPLAINT,QUALITY_COMPLAINT
    }

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    private String userName;

    private String phone;

    private String city;

    private String message;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}