package ru.jmdevelop.besshoptgbot.domain.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;


public interface FormService {
    public BotApiMethod<?> processFormInput(Long chatId, String input);
}
