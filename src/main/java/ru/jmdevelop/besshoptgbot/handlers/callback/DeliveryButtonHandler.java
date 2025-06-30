package ru.jmdevelop.besshoptgbot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.jmdevelop.besshoptgbot.handlers.ButtonHandler;

@Component
@RequiredArgsConstructor
public class DeliveryButtonHandler implements ButtonHandler {

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("📦 Доставка");
    }

    @Override
    public SendMessage handle(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("🚚 Условия доставки:\n\n" +
                        "• Курьером по Москве - 300 руб.\n" +
                        "• Самовывоз - бесплатно\n" +
                        "• Сроки: 1-3 рабочих дня")
                .build();
    }
}