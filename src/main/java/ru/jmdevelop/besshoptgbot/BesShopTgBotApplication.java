package ru.jmdevelop.besshoptgbot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.jmdevelop.besshoptgbot.Config.BotProperties;


@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class BesShopTgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BesShopTgBotApplication.class, args);
    }
}