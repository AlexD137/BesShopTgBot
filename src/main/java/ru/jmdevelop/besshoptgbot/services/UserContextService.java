package ru.jmdevelop.besshoptgbot.services;


import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;

public interface UserContextService {
    void pushMenuState(Long chatId, KeyboardType type);
    KeyboardType popMenuState(Long chatId);
    void resetMenuHistory(Long chatId);
}
