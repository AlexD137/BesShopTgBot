package ru.jmdevelop.besshoptgbot.handlers.keyboards.creators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.services.UserContextService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuCreator implements KeyboardCreator {
    private final UserContextService userContext;

    @Override
    public boolean supports(KeyboardType type) {
        return type == KeyboardType.MAIN_MENU;
    }

    @Override
    public ReplyKeyboardMarkup createKeyboard(Long chatId) {
        userContext.pushState(chatId, KeyboardType.MAIN_MENU);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(
                        new KeyboardRow(List.of(
                                new KeyboardButton("🛍️ Каталог"),
                                new KeyboardButton("🆘 Поддержка")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("⚠ Жалоба"),
                                new KeyboardButton("📢 Подписаться")
                        ))
                ))
                .resizeKeyboard(true)
                .build();
    }
}
