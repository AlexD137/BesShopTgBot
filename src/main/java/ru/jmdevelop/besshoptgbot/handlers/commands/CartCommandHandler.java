package ru.jmdevelop.besshoptgbot.handlers.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.register.CommandHandlerRegistry;
import ru.jmdevelop.besshoptgbot.models.dom.*;
import ru.jmdevelop.besshoptgbot.models.entity.Message;
import ru.jmdevelop.besshoptgbot.models.entity.Product;
import ru.jmdevelop.besshoptgbot.models.entity.Client;
import ru.jmdevelop.besshoptgbot.repo.CartRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientOrderStateRepository;
import ru.jmdevelop.besshoptgbot.repo.jpa.ClientRepository;
import ru.jmdevelop.besshoptgbot.services.CartService;
import ru.jmdevelop.besshoptgbot.services.MessageService;
import ru.jmdevelop.besshoptgbot.services.TelegramService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CartCommandHandler implements CommandHandler, UpdateHandler {


    private final CartService cartService;
    private final TelegramService telegramService;


    @Override
    public Command getCommand() {
        return Command.CART;
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        handleCartMessageUpdate(absSender, chatId);
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return isCartMessageUpdate(update) || isCallbackQueryUpdate(update);
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        if (isCartMessageUpdate(update)) {
            handleCartMessageUpdate(absSender, update.getMessage().getChatId());
        }
        if (isCallbackQueryUpdate(update)) {
            handleCallbackQueryUpdate(absSender, update);
        }
    }

    private boolean isCartMessageUpdate(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().equals(Button.CART.getAlias());
    }

    private boolean isCallbackQueryUpdate(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("cart_");
    }

    private void handleCartMessageUpdate(AbsSender absSender, Long chatId) throws TelegramApiException {
        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        cartRepository.updatePageNumberByChatId(chatId, 0);

        if (cartItems.isEmpty()) {
            sendEmptyCartMessage(absSender, chatId);
            return;
        }

        sendCartMessage(absSender, chatId, cartItems, 0);
    }

    private void handleCallbackQueryUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        if (DELETE_PRODUCT_CALLBACK.equals(data)) {
            doDeleteProduct(absSender, chatId, messageId);
        }
        if (MINUS_PRODUCT_CALLBACK.equals(data)) {
            doMinusProduct(absSender, chatId, messageId);
        }
        if (PLUS_PRODUCT_CALLBACK.equals(data)) {
            doPlusProduct(absSender, chatId, messageId);
        }
        if (PREVIOUS_PRODUCT_CALLBACK.equals(data)) {
            doPreviousProduct(absSender, chatId, messageId);
        }
        if (NEXT_PRODUCT_CALLBACK.equals(data)) {
            doNextProduct(absSender, chatId, messageId);
        }
        if (PROCESS_ORDER_CALLBACK.equals(data)) {
            doProcessOrder(absSender, update, chatId, messageId);
        }
    }

    private void doDeleteProduct(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartRepository.findPageNumberByChatId(chatId);

        if (!cartItems.isEmpty()) {
            CartItem cartItem = cartItems.get(currentCartPage);
            if (cartItem != null) {
                cartItems.remove(cartItem);
                cartRepository.deleteCartItem(chatId, cartItem.getId());
            }
        }

        if (cartItems.isEmpty()) {
            editClearedCartMessage(absSender, chatId, messageId);
            return;
        }

        if (cartItems.size() == currentCartPage) {
            currentCartPage -= 1;
            cartRepository.updatePageNumberByChatId(chatId, currentCartPage);
        }

        editCartMessage(absSender, chatId, messageId, cartItems, currentCartPage);
    }

    private void doMinusProduct(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartRepository.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            editEmptyCartMessage(absSender, chatId, messageId);
            return;
        }

        CartItem cartItem = cartItems.get(currentCartPage);

        if (cartItem == null || cartItem.getQuantity() <= 1) {
            return;
        }

        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartRepository.updateCartItem(chatId, cartItem);

        editCartMessage(absSender, chatId, messageId, cartItems, currentCartPage);
    }

    private void doPlusProduct(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartRepository.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            editEmptyCartMessage(absSender, chatId, messageId);
            return;
        }

        CartItem cartItem = cartItems.get(currentCartPage);

        if (cartItem == null || cartItem.getQuantity() >= MAX_QUANTITY_PER_PRODUCT) {
            return;
        }

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartRepository.updateCartItem(chatId, cartItem);

        editCartMessage(absSender, chatId, messageId, cartItems, currentCartPage);
    }

    private void doPreviousProduct(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartRepository.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            editEmptyCartMessage(absSender, chatId, messageId);
            return;
        }
        if (cartItems.size() == 1) {
            return;
        }

        if (currentCartPage <= 0) {
            currentCartPage = cartItems.size() - 1;
        } else {
            currentCartPage -= 1;
        }
        cartRepository.updatePageNumberByChatId(chatId, currentCartPage);

        editCartMessage(absSender, chatId, messageId, cartItems, currentCartPage);
    }

    private void doNextProduct(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartRepository.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            editEmptyCartMessage(absSender, chatId, messageId);
            return;
        }
        if (cartItems.size() == 1) {
            return;
        }

        if (currentCartPage >= cartItems.size() - 1) {
            currentCartPage = 0;
        } else {
            currentCartPage += 1;
        }
        cartRepository.updatePageNumberByChatId(chatId, currentCartPage);

        editCartMessage(absSender, chatId, messageId, cartItems, currentCartPage);
    }

    private void sendEmptyCartMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Cart is empty.")
                .build();
        absSender.execute(message);
    }

    private void editEmptyCartMessage(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Cart is empty.")
                .build();
        absSender.execute(message);
    }

    private void editClearedCartMessage(AbsSender absSender, Long chatId, Integer messageId) throws TelegramApiException {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Cart cleared.")
                .build();
        absSender.execute(message);
    }

    private void sendCartMessage(AbsSender absSender, Long chatId, List<CartItem> cartItems, int currentCartPage)
            throws TelegramApiException {

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(createProductText(cartItems.get(currentCartPage)))
                .replyMarkup(createCartKeyboard(cartItems, currentCartPage))
                .parseMode("HTML")
                .build();
        absSender.execute(message);
    }

    private void editCartMessage(AbsSender absSender, Long chatId, Integer messageId, List<CartItem> cartItems,
                                 int currentCartPage) throws TelegramApiException {

        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(createProductText(cartItems.get(currentCartPage)))
                .replyMarkup(createCartKeyboard(cartItems, currentCartPage))
                .parseMode("HTML")
                .build();
        absSender.execute(message);
    }

    private String createProductText(CartItem cartItem) {
        Message message = messageService.findByName("CART_MESSAGE");
        if (cartItem != null) {
            Product product = cartItem.getProduct();
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_NAME%", product.getName()));
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_DESCRIPTION%", product.getDescription()));
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_PRICE%", product.getPrice()));
            message.applyPlaceholder(MessagePlaceholder.of("%PRODUCT_QUANTITY%", cartItem.getQuantity()));
            message.applyPlaceholder(
                    MessagePlaceholder.of("%PRODUCT_TOTAL_PRICE%", product.getPrice() * cartItem.getQuantity()));
        }
        return message.buildText();
    }

    private InlineKeyboardMarkup createCartKeyboard(List<CartItem> cartItems, int currentCartPage) {
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setCartItems(cartItems);

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        keyboardBuilder.keyboardRow(Arrays.asList(
                InlineKeyboardButton.builder().text("\u2716").callbackData(DELETE_PRODUCT_CALLBACK).build(),
                InlineKeyboardButton.builder().text("\u2796").callbackData(MINUS_PRODUCT_CALLBACK).build(),
                InlineKeyboardButton.builder().text(cartItems.get(currentCartPage).getQuantity() + " pcs.")
                        .callbackData(PRODUCT_QUANTITY_CALLBACK).build(),
                InlineKeyboardButton.builder().text("\u2795").callbackData(PLUS_PRODUCT_CALLBACK).build()
        ));

        keyboardBuilder.keyboardRow(Arrays.asList(
                InlineKeyboardButton.builder().text("\u25c0").callbackData(PREVIOUS_PRODUCT_CALLBACK).build(),
                InlineKeyboardButton.builder().text((currentCartPage + 1) + "/" + cartItems.size())
                        .callbackData(CURRENT_PAGE_CALLBACK).build(),
                InlineKeyboardButton.builder().text("\u25b6").callbackData(NEXT_PRODUCT_CALLBACK).build()
        ));

        keyboardBuilder.keyboardRow(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(String.format("\u2705 Order for %d $ Checkout?", clientOrder.calculateTotalPrice()))
                        .callbackData(PROCESS_ORDER_CALLBACK).build()
        ));
        return keyboardBuilder.build();
    }

    private void doProcessOrder(AbsSender absSender, Update update, Long chatId, Integer messageId)
            throws TelegramApiException {

        List<CartItem> cartItems = cartRepository.findAllCartItemsByChatId(chatId);
        if (cartItems.isEmpty()) {
            editEmptyCartMessage(absSender, chatId, messageId);
            return;
        }

        editProcessOrderMessage(absSender, chatId, messageId);
        saveClientOrderState(chatId, cartItems);
        executeNextCommand(absSender, update, chatId);
    }

    private void saveClientOrderState(Long chatId, List<CartItem> cartItems) {
        Client client = clientRepository.findByChatId(chatId);

        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setCartItems(cartItems);
        clientOrder.setClientName(client.getName());
        clientOrder.setPhoneNumber(client.getPhoneNumber());
        clientOrder.setCity(client.getCity());
        clientOrder.setAddress(client.getAddress());
        clientOrderStateRepository.updateByChatId(chatId, clientOrder);
    }

    private void editProcessOrderMessage(AbsSender absSender, Long chatId, Integer messageId)
            throws TelegramApiException {

        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Creating order...")
                .build();
        absSender.execute(message);
    }

    private void executeNextCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        clientCommandStateRepository.pushByChatId(chatId, getCommand());
        commandHandlerRegistry.find(Command.ENTER_NAME).executeCommand(absSender, update, chatId);
    }

}