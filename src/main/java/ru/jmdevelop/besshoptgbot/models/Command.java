package ru.jmdevelop.besshoptgbot.models;


public enum Command {
    START("/start", "Главное меню"),
    HELP("/help", "Помощь"),
    CATALOG("/catalog", "🛍️ Каталог"),
    SUPPORT("/support", "🆘 Поддержка"),
    COMPLAINT("/complaint", "⚠ Жалоба"),
    SUBSCRIBE("/subscribe", "📢 Подписаться"),
    CART("/cart", "🛒 Корзина"),
    ORDERS("/orders", "📦 Мои заказы"),
    PROFILE("/profile", "👤 Профиль");

    private final String command;
    private final String description;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static Command fromString(String text) {
        for (Command cmd : Command.values()) {
            if (cmd.command.equalsIgnoreCase(text)) {
                return cmd;
            }
        }
        return null;
    }
}
