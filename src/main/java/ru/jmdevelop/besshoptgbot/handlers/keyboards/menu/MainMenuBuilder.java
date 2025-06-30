package ru.jmdevelop.besshoptgbot.handlers.keyboards.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class MainMenuBuilder {

    public ReplyKeyboardMarkup buildMainMenu() {
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(
                        new KeyboardRow(List.of(
                                new KeyboardButton("📞 Каталог")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("📞 Техническая поддержка")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("ℹ️ Оставить жалобу"),
                                new KeyboardButton("📞 Оставить отзыв")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("📞 Подписаться на канал")
                        ))
                ))
                .resizeKeyboard(true)
                .build();
    }

}