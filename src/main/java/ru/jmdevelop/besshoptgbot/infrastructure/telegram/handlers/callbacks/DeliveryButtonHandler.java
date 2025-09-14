package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.callbacks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.UpdateHandler;

@Component
@RequiredArgsConstructor
public class DeliveryButtonHandler implements UpdateHandler {



    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("📦 Доставка");
    }

    @Override
    public BotApiMethod<?> handle(Update update) throws TelegramApiException {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("🚚 Условия доставки:\n\n" +
                        "• Курьером по Москве - 300 руб.\n" +
                        "• Самовывоз - бесплатно\n" +
                        "• Сроки: 1-3 рабочих дня")
                .build();
    }
}