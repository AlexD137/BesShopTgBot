package ru.jmdevelop.besshoptgbot.handlers.keyboards.creators;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;

public interface KeyboardCreator {
    boolean supports(KeyboardType type);
    ReplyKeyboardMarkup createKeyboard(Long chatId);
}