package ru.jmdevelop.besshoptgbot.handlers.keyboards.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.creators.KeyboardCreator;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.creators.SubscriptionMenuCreator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardFactory {
    private final List<KeyboardCreator> creators;
    private final SubscriptionMenuCreator subscriptionCreator;

    public ReplyKeyboardMarkup create(KeyboardType type, Long chatId) {
        if (type == KeyboardType.SUBSCRIPTION) {
            return null;
        }

        return creators.stream()
                .filter(creator -> creator.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No creator for: " + type))
                .createKeyboard(chatId);
    }

    public InlineKeyboardMarkup createSubscriptionKeyboard(String channelUrl) {
        return subscriptionCreator.createInlineKeyboard(channelUrl);
    }
}
