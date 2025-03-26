package ru.jmdevelop.besshoptgbot.repo.database;

import ru.jmdevelop.besshoptgbot.models.entity.Client;
import ru.jmdevelop.besshoptgbot.repo.ClientRepository;

import static ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory.inTransaction;
import static ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory.inTransactionVoid;

public class ClientRepositoryDefault implements ClientRepository {

    @Override
    public Client findByChatId(Long chatId) {
        String query = "from Client where chatId = :chatId";

        return inTransaction(session ->
                session.createQuery(query, Client.class)
                        .setParameter("chatId", chatId)
                        .setMaxResults(1)
                        .uniqueResult()
        );
    }

    @Override
    public void save(Client client) {
        inTransactionVoid(session -> session.persist(client));
    }

    @Override
    public void update(Client client) {
        inTransactionVoid(session -> session.merge(client));
    }

}