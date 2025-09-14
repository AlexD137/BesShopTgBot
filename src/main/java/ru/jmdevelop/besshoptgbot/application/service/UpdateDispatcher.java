package ru.jmdevelop.besshoptgbot.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.jmdevelop.besshoptgbot.domain.services.FormService;
import ru.jmdevelop.besshoptgbot.domain.services.UserStateService;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.UpdateHandler;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher  implements LongPollingUpdateConsumer {
    private final TelegramClient telegramClient;
    private final List<UpdateHandler> handlers;
    private final ErrorHandler errorHandler;


    private final UserStateService userStateService;
    private final FormService formService;



    @Override
    public void consume(List<Update> updates) {
        updates.forEach(update -> {
            try {
                log.debug("Processing update ID: {}", update.getUpdateId());
                dispatch(update);
            } catch (Exception e) {
                log.error("Error processing update {}", update.getUpdateId(), e);
            }

        });
    }

    public void dispatch(Update update) {
        try {
            if (isFormUpdate(update)) {
                handleFormUpdate(update);
                return;
            }
            for (UpdateHandler handler : handlers) {
                if (handler.canHandle(update)) {
                    BotApiMethod<?> response = handler.handle(update);
                    if (response != null) {
                        telegramClient.execute(response);
                    }
                    return;
                }
            }

            handleUnrecognizedCommand(update);
        } catch (TelegramApiException e) {
            errorHandler.handle(update, e);
        }
    }

    private boolean isFormUpdate(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return false;
        Long chatId = update.getMessage().getChatId();
        return userStateService.isUserInForm(chatId);    }

    private void handleFormUpdate(Update update) {
        try {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            BotApiMethod<?> response = formService.processFormInput(chatId, text);
            if (response != null) {
                telegramClient.execute(response);
            }
        } catch (TelegramApiException e) {
            log.error("Failed to process form update", e);
            errorHandler.handle(update, e);
        }
    }


    private void handleUnrecognizedCommand(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        SendMessage message = new SendMessage(
                update.getMessage().getChatId().toString(),
                "Команда не распознана. Введите /help для списка команд"
        );

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send unrecognized command message", e);
        }
    }


}