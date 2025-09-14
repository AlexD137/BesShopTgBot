package ru.jmdevelop.besshoptgbot.domain.services;


import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardType;

public interface UserContextService {
    void pushMenuState(Long chatId, KeyboardType type);
    KeyboardType popMenuState(Long chatId);
    void resetMenuHistory(Long chatId);
}
