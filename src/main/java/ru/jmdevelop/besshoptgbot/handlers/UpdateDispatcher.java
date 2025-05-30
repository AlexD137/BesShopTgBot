package ru.jmdevelop.besshoptgbot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.exceptions.ErrorHandler;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final List<UpdateHandler> handlers;
    private final ErrorHandler errorHandler;

    public void dispatch(Update update, AbsSender sender) {
        try {
            Optional<UpdateHandler> handler = handlers.stream()
                    .filter(h -> h.canHandle(update))
                    .findFirst();

            handler.ifPresentOrElse(
                    h -> executeHandler(h, update, sender),
                    () -> handleFallback(update, sender)
            );
        } catch (Exception e) {
            errorHandler.handle(update, e);
        }
    }

    private void executeHandler(UpdateHandler handler, Update update, AbsSender sender) {
        try {
            handler.handle(update, sender);
        } catch (TelegramApiException e) {
            throw new UpdateProcessingException("Handler execution failed", e);
        }
    }

    private void handleFallback(Update update, AbsSender sender) {
        if (update.hasMessage()) {
            try {
                sender.execute(
                        new SendMessage(update.getMessage().getChatId().toString(),
                                "Команда не распознана")
                );
            } catch (TelegramApiException e) {
                log.error("Fallback message failed", e);
            }
        }
    }
}