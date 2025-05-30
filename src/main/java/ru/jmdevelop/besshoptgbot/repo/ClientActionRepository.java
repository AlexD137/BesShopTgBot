package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;

public interface ClientActionRepository {
    ClientAction findByChatId(Long chatId);
    void updateByChatId(Long chatId, ClientAction action);
    void deleteByChatId(Long chatId);
}