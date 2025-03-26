package ru.jmdevelop.besshoptgbot.services;

import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.models.entity.Order;

public interface NotificationService {

    void notifyAdminChatAboutNewOrder(AbsSender absSender, Order order) throws TelegramApiException;

}
