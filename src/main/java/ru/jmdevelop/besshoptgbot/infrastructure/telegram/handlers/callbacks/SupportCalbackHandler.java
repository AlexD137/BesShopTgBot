package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.callbacks;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardFactory;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardType;


@Component
@RequiredArgsConstructor
public class SupportCalbackHandler implements UpdateHandler {

    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("🆘 Поддержка");

    }

    @Override
    public BotApiMethod<?> handle(Update update) throws TelegramApiException {

        ReplyKeyboardMarkup keyboard = keyboardFactory.create(
                KeyboardType.SUPPORT, update.getMessage().getChatId()
        );
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Выберите раздел")
                .replyMarkup(keyboard)
                .build();
    }

}