package ru.jmdevelop.besshoptgbot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.jmdevelop.besshoptgbot.Config.BotProperties;
import ru.jmdevelop.besshoptgbot.main.TelegramBot;


@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class BesShopTgBotApplication {
    private static final Logger log = LoggerFactory.getLogger(BesShopTgBotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BesShopTgBotApplication.class, args);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            log.info("Telegram bot registered successfully");
            return botsApi;
        } catch (TelegramApiException e) {
            log.error("Failed to register bot", e);
            throw new RuntimeException("Bot registration failed", e);
        }
    }
}