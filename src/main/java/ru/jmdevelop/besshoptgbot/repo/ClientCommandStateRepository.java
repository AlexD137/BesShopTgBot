package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.dom.Command;

public interface ClientCommandStateRepository {

    void pushByChatId(Long chatId, Command command);

    Command popByChatId(Long chatId);

    void deleteAllByChatId(Long chatId);

}
