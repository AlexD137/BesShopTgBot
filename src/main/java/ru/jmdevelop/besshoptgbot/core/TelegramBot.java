package ru.jmdevelop.besshoptgbot.core;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import ru.jmdevelop.besshoptgbot.Config.BotProperties;





@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot {
    private final BotProperties properties;
    private final LongPollingUpdateConsumer updateConsumer;

    @Override
    public String getBotToken() {
        return properties.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
