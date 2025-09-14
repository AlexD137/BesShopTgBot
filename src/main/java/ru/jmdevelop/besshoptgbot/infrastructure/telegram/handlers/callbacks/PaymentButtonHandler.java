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
public class PaymentButtonHandler implements UpdateHandler {



    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("💳 Оплата");
    }

    @Override
    public BotApiMethod<?> handle(Update update) throws TelegramApiException {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("💳 Способы оплаты:\n\n" +
                        "1. Онлайн-картой (Visa/Mastercard)\n" +
                        "2. При получении (наличными или картой)\n" +
                        "3. По счету для юр. лиц")
                .build();
    }
}