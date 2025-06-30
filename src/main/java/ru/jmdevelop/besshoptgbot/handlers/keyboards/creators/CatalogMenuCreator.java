package ru.jmdevelop.besshoptgbot.handlers.keyboards.creators;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CatalogMenuCreator implements KeyboardCreator {

//    @Value("${telegram.webapp.url}")
//    private String webAppUrl;

    @Override
    public boolean supports(KeyboardType type) {
        return type == KeyboardType.CATALOG;
    }

    @Override
    public ReplyKeyboardMarkup createKeyboard(Long chatId) {
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(
                        new KeyboardRow(List.of(
                                new KeyboardButton("\uD83D\uDCBC  Юр. Информация")
                        )),
                        new KeyboardRow(List.of(
                                new KeyboardButton("📦 Доставка"),
                                new KeyboardButton("💳 Оплата")
                        )),
//                        new KeyboardRow(List.of(
//                                KeyboardButton.builder()
//                                        .text("\uD83D\uDECD\uFE0F Открыть каталог")
//                                        .webApp(new WebAppInfo(webAppUrl))
//                                        .build()
//                        )),
                                new KeyboardRow(List.of(
                                        new KeyboardButton("\uD83D\uDD19 Назад")
                                ))
                        )).resizeKeyboard(true)
                                .build();
    }
}
