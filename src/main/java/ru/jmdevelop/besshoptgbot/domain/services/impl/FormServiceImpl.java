package ru.jmdevelop.besshoptgbot.domain.services.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import ru.jmdevelop.besshoptgbot.domain.services.FormService;

@Service
public class FormServiceImpl implements FormService {
    @Override
    public BotApiMethod<?> processFormInput(Long chatId, String input) {
        return null;
    }
}
