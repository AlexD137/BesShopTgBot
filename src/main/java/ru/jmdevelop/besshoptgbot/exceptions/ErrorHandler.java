package ru.jmdevelop.besshoptgbot.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class ErrorHandler {
    public void handle(Update update, Exception e) {
        String errorDetails = String.format(
                "UpdateID: %d | ChatID: %s | Text: '%s' | Error: %s",
                update.getUpdateId(),
                update.hasMessage() ? update.getMessage().getChatId() : "N/A",
                update.hasMessage() && update.getMessage().hasText()
                        ? update.getMessage().getText()
                        : "N/A",
                e != null ? e.getMessage() : "Null exception"
        );

        log.error("Processing failed:\n{}", errorDetails);
        if (e != null) {
            log.error("Stack trace:", e);
        }
    }
}