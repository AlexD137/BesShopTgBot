package ru.jmdevelop.besshoptgbot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.models.HandlerPriority;

public interface UpdateHandler {
    boolean canHandle(Update update);
    void handle(Update update, AbsSender sender) throws TelegramApiException;
    default int getPriority() {
        return HandlerPriority.USER_INPUT.getPriority();
    }
}