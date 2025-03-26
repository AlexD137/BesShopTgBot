package ru.jmdevelop.besshoptgbot.repo.database;

import ru.jmdevelop.besshoptgbot.models.entity.Message;
import ru.jmdevelop.besshoptgbot.repo.MessageRepository;

import static ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory.inTransaction;

public class MessageRepositoryDefault implements MessageRepository {

    @Override
    public Message findByName(String messageName) {
        String query = "from Message where name = :name";

        return inTransaction(session ->
                session.createQuery(query, Message.class)
                        .setParameter("name", messageName)
                        .setMaxResults(1)
                        .uniqueResult()
        );
    }

}