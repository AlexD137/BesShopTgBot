package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.entity.Client;

public interface ClientRepository {

    Client findByChatId(Long chatId);

    void save(Client client);

    void update(Client client);

}