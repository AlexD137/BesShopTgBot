package ru.jmdevelop.besshoptgbot.services;


import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;

public interface UserContextService {
    void pushState(Long chatId, KeyboardType type);
    KeyboardType peekState(Long chatId);
    KeyboardType popState(Long chatId);
    void resetContext(Long chatId);
    void putTempData(Long chatId, String key, Object value);
    <T> T getTempData(Long chatId, String key, Class<T> type);
    void cleanOldSessions();
}
