package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.creators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.domain.services.UserContextService;

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
        userContext.pushMenuState(chatId, KeyboardType.MAIN_MENU);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(
                        new KeyboardRow(List.of(
                                new KeyboardButton("\uD83D\uDECD\uFE0F Каталог")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("🆘 Поддержка")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("\uD83D\uDE10 Оставить Жалобу"),
                                new KeyboardButton("\uD83D\uDE00 Оставить отзыв")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("📢 Подписаться")
                        ))
                ))
                .resizeKeyboard(true)
                .build();
    }
}
