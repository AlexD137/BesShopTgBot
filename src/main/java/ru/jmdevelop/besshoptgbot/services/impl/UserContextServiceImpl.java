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
    private static final int MAX_HISTORY_SIZE = 20;
    private final Map<Long, Deque<KeyboardType>> menuHistory = new ConcurrentHashMap<>();

    @Override
    public void pushMenuState(Long chatId, KeyboardType type) {
        if (chatId == null || type == null) {
            log.error("Null parameters: chatId={}, type={}", chatId, type);
            return;
        }

        menuHistory.compute(chatId, (k, deque) -> {
            if (deque == null) {
                deque = new ArrayDeque<>();
            }
            if (deque.size() >= MAX_HISTORY_SIZE) {
                deque.removeLast();
            }
            deque.push(type);
            log.debug("Pushed menu state for chat {}: {}", chatId, type);
            return deque;
        });
    }

    @Override
    public KeyboardType popMenuState(Long chatId) {
        if (chatId == null) return KeyboardType.MAIN_MENU;

        Deque<KeyboardType> history = menuHistory.get(chatId);
        if (history == null || history.isEmpty()) {
            return KeyboardType.MAIN_MENU;
        }

        if (history.size() == 1) {
            return history.peek();
        }

        KeyboardType removed = history.pop();
        KeyboardType newCurrent = history.peek();
        log.debug("Menu changed for chat {}: {} -> {}", chatId, removed, newCurrent);
        return newCurrent;
    }

    @Override
    public void resetMenuHistory(Long chatId) {
        if (chatId == null) return;

        Deque<KeyboardType> newHistory = new ArrayDeque<>();
        newHistory.push(KeyboardType.MAIN_MENU);
        menuHistory.put(chatId, newHistory);
        log.debug("Reset menu history for chat {}", chatId);
    }


}
