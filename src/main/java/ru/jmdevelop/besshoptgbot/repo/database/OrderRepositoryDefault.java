package ru.jmdevelop.besshoptgbot.repo.database;

import ru.jmdevelop.besshoptgbot.models.entity.Order;
import ru.jmdevelop.besshoptgbot.repo.OrderRepository;

import static ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory.inTransactionVoid;

public class OrderRepositoryDefault implements OrderRepository {

    @Override
    public void save(Order order) {
        inTransactionVoid(session -> session.persist(order));
    }

}