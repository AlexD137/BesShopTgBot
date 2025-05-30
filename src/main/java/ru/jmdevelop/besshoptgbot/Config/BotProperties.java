package ru.jmdevelop.besshoptgbot.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
public record BotProperties(
        String username,
        String token
) {
}