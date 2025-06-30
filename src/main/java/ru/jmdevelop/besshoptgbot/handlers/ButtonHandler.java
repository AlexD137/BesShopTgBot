package ru.jmdevelop.besshoptgbot.handlers;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public interface ButtonHandler {
    boolean canHandle(String buttonText);
    SendMessage handle(Long chatId);
}