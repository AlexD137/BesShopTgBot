package ru.jmdevelop.besshoptgbot.models.dom;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardFactory {


    public InlineKeyboardMarkup createCartKeyboard(int currentQuantity, int currentPage, int totalPages, double totalPrice) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();


        keyboard.add(createQuantityRow(currentQuantity));

        if (totalPages > 1) {
            keyboard.add(createNavigationRow(currentPage, totalPages));        }


        keyboard.add(createCheckoutRow(totalPrice));

        return new InlineKeyboardMarkup(keyboard);
    }


    private List<InlineKeyboardButton> createQuantityRow(int quantity) {
        return List.of(
                createButton("❌ Удалить", CartConstants.DELETE_PRODUCT),
                createButton("➖", CartConstants.MINUS_PRODUCT),
                createButton(quantity + " шт.", CartConstants.PRODUCT_QUANTITY),
                createButton("➕", CartConstants.PLUS_PRODUCT)
        );
    }


    private List<InlineKeyboardButton> createNavigationRow(int currentPage, int totalPages) {
        return List.of(
                createButton("◀️", CartConstants.PREVIOUS_PRODUCT),
                createButton((currentPage + 1) + "/" + totalPages, CartConstants.CURRENT_PAGE),
                createButton("▶️", CartConstants.NEXT_PRODUCT)
        );
    }


    private List<InlineKeyboardButton> createCheckoutRow(double totalPrice) {
        return List.of(
                createButton(String.format("✅ Оформить заказ (%.2f ₽)", totalPrice),
                        CartConstants.PROCESS_ORDER)
        );
    }


    private InlineKeyboardButton createButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }


    public InlineKeyboardMarkup createEmptyCartKeyboard() {
        return new InlineKeyboardMarkup(List.of(
                List.of(createButton("Вернуться в меню", "menu=main"))
        ));
    }
}