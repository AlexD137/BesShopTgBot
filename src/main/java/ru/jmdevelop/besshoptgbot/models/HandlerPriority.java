package ru.jmdevelop.besshoptgbot.models;

public enum HandlerPriority {
    SYSTEM(100),
    USER_INPUT(70),
    PAYMENT(50),
    PROMO(30),
    FALLBACK(0);

    private final int priority;

    HandlerPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
