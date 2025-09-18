package ru.jmdevelop.besshoptgbot.domain.models.user;

import lombok.Data;
import ru.jmdevelop.besshoptgbot.domain.models.Cart;
import ru.jmdevelop.besshoptgbot.domain.models.Order;
import ru.jmdevelop.besshoptgbot.domain.models.Role;

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
    private Map<Long, Map<FormType, String>> appeals;
    private LocalDate birthDate;
    private LocalDateTime firstActivity;
    private LocalDateTime lastActivity;
    private boolean isActive;
    private Role role;
    private boolean inForm;
    private boolean havePermissions;
    private List<Order> orders = new ArrayList<>();
    private Cart cart;


}

