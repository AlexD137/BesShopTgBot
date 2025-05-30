package ru.jmdevelop.besshoptgbot.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.jmdevelop.besshoptgbot.models.dom.CartItem;
import ru.jmdevelop.besshoptgbot.services.TelegramService;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final AbsSender absSender;
    private final KeyboardFactory keyboardFactory;

    @Override
    public void sendCartMessage(Long chatId, CartItem item, int currentPage, int totalPages) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(formatItem(item));
        message.setReplyMarkup(keyboardFactory.createCartKeyboard(currentPage, totalPages));
        absSender.execute(message);
    }

    @Override
    public void editCartMessage(Long chatId, Integer messageId, CartItem item, int currentPage, int totalPages) {

    }

    @Override
    public void sendTextMessage(Long chatId, String text) {

    }

    @Override
    public void sendErrorMessage(Long chatId) {

    }

    @Override
    public void sendCartMessage(Long chatId, CartItem item, int currentPage, int totalPages) {

    }
}
