package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.creators;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardType;

public interface KeyboardCreator {
    boolean supports(KeyboardType type);
    ReplyKeyboardMarkup createKeyboard(Long chatId);
}