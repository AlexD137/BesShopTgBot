package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;

public interface ClientActionRepository {

    ClientAction findByChatId(Long chatId);

    void updateByChatId(Long chatId, ClientAction clientAction);

    void deleteByChatId(Long chatId);

}