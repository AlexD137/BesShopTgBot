package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.entity.Order;

public interface OrderRepository {

    void save(Order order);

}