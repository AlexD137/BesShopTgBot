package ru.jmdevelop.besshoptgbot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface FormHandler {
    boolean isProcessingForm(Long chatId);
    SendMessage handleFormInput(Long chatId, String text);
}