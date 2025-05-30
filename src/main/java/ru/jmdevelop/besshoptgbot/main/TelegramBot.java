package ru.jmdevelop.besshoptgbot.main;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.Config.BotProperties;
import ru.jmdevelop.besshoptgbot.exceptions.HandlerNotFoundException;
import ru.jmdevelop.besshoptgbot.handlers.ActionHandler;
import ru.jmdevelop.besshoptgbot.handlers.UpdateDispatcher;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {



    private final BotProperties properties;
    private final UpdateDispatcher dispatcher;



    @Override
    public String getBotUsername() {
        return properties.username();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            log.debug("Received update: {}", update);
            dispatcher.dispatch(update, this);
        } catch (Exception e) {
            log.error("Failed to process update {}", update.getUpdateId(), e);
            sendFallbackResponse(update);
        }
    }

    private void sendFallbackResponse(Update update) {
        try {
            if (update.hasMessage()) {
                execute(new SendMessage(update.getMessage().getChatId().toString(),
                        "⚠ Произошла ошибка. Попробуйте позже или обратитесь в поддержку."));
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send error message", e);
        }
    }



}
