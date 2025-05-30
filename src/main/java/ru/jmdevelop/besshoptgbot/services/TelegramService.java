package ru.jmdevelop.besshoptgbot.services;

import ru.jmdevelop.besshoptgbot.models.dom.CartItem;

public interface TelegramService {
    void sendCartMessage(Long chatId, CartItem item, int currentPage, int totalPages);
    void editCartMessage(Long chatId, Integer messageId, CartItem item, int currentPage, int totalPages);
    void sendTextMessage(Long chatId, String text);
    void sendErrorMessage(Long chatId);
}