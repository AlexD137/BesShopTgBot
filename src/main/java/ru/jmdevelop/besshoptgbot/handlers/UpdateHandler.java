package ru.jmdevelop.besshoptgbot.handlers;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface UpdateHandler {
    boolean canHandle(Update update);
    BotApiMethod<?> handle(Update update) throws TelegramApiException;
}