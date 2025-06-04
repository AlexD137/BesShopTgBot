package ru.jmdevelop.besshoptgbot.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.services.UserContextService;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserContextServiceImpl implements UserContextService {
    private final Map<Long, Deque<KeyboardType>> userStates = new ConcurrentHashMap<>();
    private final Map<String, Object> tempDataStorage = new ConcurrentHashMap<>();

    @Override
    public void pushState(Long chatId, KeyboardType type) {
        userStates.computeIfAbsent(chatId, k -> new ArrayDeque<>()).push(type);
        log.debug("[Context] Pushed state for {}: {}", chatId, type);
    }

    @Override
    public KeyboardType peekState(Long chatId) {
        Deque<KeyboardType> stack = userStates.get(chatId);
        return (stack != null && !stack.isEmpty()) ? stack.peek() : KeyboardType.MAIN_MENU;
    }

    @Override
    public KeyboardType popState(Long chatId) {
        Deque<KeyboardType> stack = userStates.get(chatId);
        if (stack == null || stack.isEmpty()) {
            return KeyboardType.MAIN_MENU;
        }
        KeyboardType previous = stack.pop();
        log.debug("[Context] Popped state for {}: {}", chatId, previous);
        return previous;
    }

    @Override
    public void resetContext(Long chatId) {
        userStates.remove(chatId);
        tempDataStorage.keySet().removeIf(key -> key.startsWith(chatId + "_"));
        log.info("[Context] Reset context for {}", chatId);
    }

    @Override
    public void putTempData(Long chatId, String key, Object value) {
        tempDataStorage.put(createCompositeKey(chatId, key), value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getTempData(Long chatId, String key, Class<T> type) {
        return (T) tempDataStorage.get(createCompositeKey(chatId, key));
    }

    @Override
    @Scheduled(fixedRate = 3_600_000) // Каждый час
    public void cleanOldSessions() {
        long cutoff = System.currentTimeMillis() - 86_400_000; // 24 часа
        userStates.keySet().removeIf(chatId -> {
            // Логика определения неактивности
            return true; // Заглушка для реализации
        });
        log.info("[Context] Cleaned old sessions");
    }

    private String createCompositeKey(Long chatId, String key) {
        return chatId + "_" + key;
    }
}