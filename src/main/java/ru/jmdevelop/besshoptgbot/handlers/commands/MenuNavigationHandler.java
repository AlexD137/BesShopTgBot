package ru.jmdevelop.besshoptgbot.handlers.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardFactory;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.services.UserContextService;



@Component
@RequiredArgsConstructor
public class MenuNavigationHandler implements UpdateHandler {

    private final UserContextService userContext;
    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        String text = update.getMessage().getText().trim();
        return isNavigationButton(text);
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        if (text.equals("\uD83D\uDD19 Назад")) {
            KeyboardType previousMenu = userContext.popMenuState(chatId);
            return createMenuResponse(
                    chatId,
                    previousMenu != null ? previousMenu : KeyboardType.MAIN_MENU
            );
        }

        KeyboardType nextMenu = resolveMenuType(text);
        userContext.pushMenuState(chatId, nextMenu);
        return createMenuResponse(chatId, nextMenu);
    }

    private boolean isNavigationButton(String text) {
        return text.equals("\uD83D\uDD19 Назад") ||
                text.equals("\uD83D\uDECD\uFE0F Каталог") ||
                text.equals("🆘 Поддержка") ||
                text.equals("😐 Оставить Жалобу") ||
                text.equals("😀 Оставить отзыв") ||
                text.equals("📢 Подписаться");
    }

    private KeyboardType resolveMenuType(String text) {
        return switch (text) {
            case "\uD83D\uDECD\uFE0F Каталог" -> KeyboardType.CATALOG;
            case "🆘 Поддержка" -> KeyboardType.SUPPORT;
            case "😐 Оставить Жалобу" -> KeyboardType.COMPLAINT;
            case "😀 Оставить отзыв" -> KeyboardType.MAIN_MENU;
            default -> KeyboardType.MAIN_MENU;
        };
    }

    private SendMessage createMenuResponse(Long chatId, KeyboardType type) {
        String title = switch (type) {
            case MAIN_MENU -> "Главное меню:";
            case CATALOG -> "Выберите раздел:";
            case SUPPORT -> "Выберите раздел:";
            case COMPLAINT -> "Выберите раздел:";
        };

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(title)
                .replyMarkup(keyboardFactory.create(type, chatId))
                .build();
    }
}
