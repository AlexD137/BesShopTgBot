package ru.jmdevelop.besshoptgbot.domain.models.user;

import lombok.Data;
import ru.jmdevelop.besshoptgbot.domain.models.cart.Cart;
import ru.jmdevelop.besshoptgbot.domain.models.order.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private LocalDateTime firstActivity;
    private LocalDateTime lastActivity;
    private boolean isActive;
    private Role role;
    private boolean inForm;
    private boolean havePermissions;
    private List<Order> orders = new ArrayList<>();
    private Cart cart;
    private List<Notification> notifications = new ArrayList<>();


}

