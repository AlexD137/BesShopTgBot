package ru.jmdevelop.besshoptgbot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.jmdevelop.besshoptgbot.exceptions.ErrorHandler;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final TelegramClient telegramClient;
    private final List<UpdateHandler> handlers;
    private final ErrorHandler errorHandler;
    private final List<ButtonHandler> buttonHandlers;
    private final List<FormHandler> formHandlers;

    public void dispatch(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Long chatId = update.getMessage().getChatId();
                String text = update.getMessage().getText();

                for (FormHandler formHandler : formHandlers) {
                    if (formHandler.isProcessingForm(chatId)) {
                        SendMessage response = formHandler.handleFormInput(chatId, text);
                        if (response != null) {
                            telegramClient.execute(response);
                            return;
                        }
                    }
                }
            }
            Optional<UpdateHandler> updateHandler = findSuitableUpdateHandler(update);
            if (updateHandler.isPresent()) {
                executeHandler(updateHandler.get(), update);
                return;
            }

            Optional<ButtonHandler> buttonHandler = findSuitableButtonHandler(update);
            if (buttonHandler.isPresent()) {
                executeButtonHandler(buttonHandler.get(), update);
                return;
            }

            handleUnrecognizedCommand(update);
        } catch (Exception e) {
            errorHandler.handle(update, e);
        }
    }


    private Optional<UpdateHandler> findSuitableUpdateHandler(Update update) {
        return handlers.stream()
                .filter(h -> h.canHandle(update))
                .findFirst();
    }

    private Optional<ButtonHandler> findSuitableButtonHandler(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return Optional.empty();
        }
        String text = update.getMessage().getText();
        return buttonHandlers.stream()
                .filter(h -> h.canHandle(text))
                .findFirst();
    }

    private void executeHandler(UpdateHandler handler, Update update) throws TelegramApiException {
        BotApiMethod<?> response = handler.handle(update);
        if (response != null) {
            telegramClient.execute(response);
        }
    }

    private void executeButtonHandler(ButtonHandler handler, Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();
        SendMessage response = handler.handle(chatId);
        if (response != null) {
            telegramClient.execute(response);
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