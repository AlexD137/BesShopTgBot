package ru.jmdevelop.besshoptgbot.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class ErrorHandler {
    public void handle(Update update, Exception e) {
        log.error("Update {} processing failed: {}", update.getUpdateId(), e.getMessage());
    }
}