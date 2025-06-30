package ru.jmdevelop.besshoptgbot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.jmdevelop.besshoptgbot.handlers.ButtonHandler;

@Component
@RequiredArgsConstructor
public class PaymentButtonHandler implements ButtonHandler {

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("💳 Оплата");
    }

    @Override
    public SendMessage handle(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("💳 Способы оплаты:\n\n" +
                        "1. Онлайн-картой (Visa/Mastercard)\n" +
                        "2. При получении (наличными или картой)\n" +
                        "3. По счету для юр. лиц")
                .build();
    }
}