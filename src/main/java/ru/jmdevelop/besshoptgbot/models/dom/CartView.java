package ru.jmdevelop.besshoptgbot.models.dom;

public record CartView(
        CartItem currentItem,
        int currentPosition,
        int totalItems,
        double totalPrice
) {
    public boolean isEmpty() {
        return currentItem == null;
    }
}