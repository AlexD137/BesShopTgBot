package ru.jmdevelop.besshoptgbot.handlers.keyboards.creators;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;

import java.util.List;

@Component
public class SubscriptionMenuCreator implements KeyboardCreator {
    @Override
    public boolean supports(KeyboardType type) {
        return type == KeyboardType.SUBSCRIPTION;
    }

    @Override
    public ReplyKeyboardMarkup createKeyboard(Long chatId) {

        return null;
    }

    public InlineKeyboardMarkup createInlineKeyboard(String channelUrl) {
        return new InlineKeyboardMarkup(List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("Подписаться")
                        .url(channelUrl)
                        .build())
        ));
    }
}
