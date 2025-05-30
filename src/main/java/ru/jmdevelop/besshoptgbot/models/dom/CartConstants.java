package ru.jmdevelop.besshoptgbot.models.dom;

public final class CartConstants {

    public static final String CALLBACK_PREFIX = "cart=";


    public static final String PRODUCT_QUANTITY = CALLBACK_PREFIX + "product-quantity";
    public static final String CURRENT_PAGE = CALLBACK_PREFIX + "current-page";
    public static final String DELETE_PRODUCT = CALLBACK_PREFIX + "delete-product";
    public static final String MINUS_PRODUCT = CALLBACK_PREFIX + "minus-product";
    public static final String PLUS_PRODUCT = CALLBACK_PREFIX + "plus-product";
    public static final String PREVIOUS_PRODUCT = CALLBACK_PREFIX + "previous-product";
    public static final String NEXT_PRODUCT = CALLBACK_PREFIX + "next-product";
    public static final String PROCESS_ORDER = CALLBACK_PREFIX + "process-order";

    public static final int MAX_QUANTITY_PER_PRODUCT = 50;

    public static boolean isCartCallback(String callbackData) {
        return callbackData != null && callbackData.startsWith(CALLBACK_PREFIX);
    }

    private CartConstants() {}
}