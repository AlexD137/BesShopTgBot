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
public class LegalInfoButtonHandler implements UpdateHandler {


    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("\uD83D\uDCBC  Юр. Информация");
    }

    @Override
    public BotApiMethod<?> handle(Update update) throws TelegramApiException {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("📝 Юридическая информация:\n\n" +
                        "1. Название компании: ООО «Пример»\n" +
                        "2. ИНН: 1234567890\n" +
                        "3. Адрес: г. Москва, ул. Примерная, 1")
                .build();
    }
}