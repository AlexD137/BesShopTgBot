package ru.jmdevelop.besshoptgbot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.jmdevelop.besshoptgbot.handlers.ButtonHandler;

@Component
@RequiredArgsConstructor
public class LegalInfoButtonHandler implements ButtonHandler {

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("\uD83D\uDCBC  Юр. Информация");
    }

    @Override
    public SendMessage handle(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("📝 Юридическая информация:\n\n" +
                        "1. Название компании: ООО «Пример»\n" +
                        "2. ИНН: 1234567890\n" +
                        "3. Адрес: г. Москва, ул. Примерная, 1")
                .build();
    }
}