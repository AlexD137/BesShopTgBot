package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.creators.KeyboardCreator;


import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardFactory {
    private final List<KeyboardCreator> creators;

    public ReplyKeyboardMarkup create(KeyboardType type, Long chatId) {

        return creators.stream()
                .filter(creator -> creator.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No creator for: " + type))
                .createKeyboard(chatId);
    }
}
