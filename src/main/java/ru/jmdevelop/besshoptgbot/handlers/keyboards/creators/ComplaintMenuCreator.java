package ru.jmdevelop.besshoptgbot.handlers.keyboards.creators;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;

import java.util.List;

@Component
public class ComplaintMenuCreator implements KeyboardCreator {
    @Override
    public boolean supports(KeyboardType type) {
        return type == KeyboardType.COMPLAINT;
    }

    @Override
    public ReplyKeyboardMarkup createKeyboard(Long chatId) {
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(
                                new KeyboardRow(List.of(
                                        new KeyboardButton("🚚 Жалоба на доставку"),
                                        new KeyboardButton("🧐 Жалоба на качество")
                                )),
                                new KeyboardRow(List.of(
                                        new KeyboardButton("🔙 Назад")
                                ))
                        ))
                        .resizeKeyboard(true)
                        .build();
    }
}
