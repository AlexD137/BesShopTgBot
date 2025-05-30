package ru.jmdevelop.besshoptgbot.handlers.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.ActionHandler;
import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.models.dom.Button;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.models.dom.ClientOrder;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.models.entity.Client;
import ru.jmdevelop.besshoptgbot.models.entity.Order;
import ru.jmdevelop.besshoptgbot.models.entity.OrderItem;
import ru.jmdevelop.besshoptgbot.models.entity.OrderStatus;
import ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory;
import ru.jmdevelop.besshoptgbot.repo.jpa.ClientRepository;
import ru.jmdevelop.besshoptgbot.repo.jpa.OrderRepository;
import ru.jmdevelop.besshoptgbot.repo.CartRepository;
import ru.jmdevelop.besshoptgbot.services.MessageService;
import ru.jmdevelop.besshoptgbot.services.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderConfirmCommandHandler implements CommandHandler, ActionHandler {

    private static final String CONFIRM_ORDER_ACTION = "order=confirm";

    private final ClientActionRepository clientActionRepository;
    private final ClientCommandStateRepository clientCommandStateRepository;
    private final ClientOrderStateRepository clientOrderStateRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final MessageService messageService;
    private final NotificationService notificationService;

    public OrderConfirmCommandHandler(
            ClientActionRepository clientActionRepository,
            ClientCommandStateRepository clientCommandStateRepository,
            ClientOrderStateRepository clientOrderStateRepository,
            CartRepository cartRepository,
            OrderRepository orderRepository,
            ClientRepository clientRepository,
            MessageService messageService,
            NotificationService notificationService) {

        this.clientActionRepository = clientActionRepository;
        this.clientCommandStateRepository = clientCommandStateRepository;
        this.clientOrderStateRepository = clientOrderStateRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @Override
    public Command getCommand() {
        return Command.ORDER_CONFIRM;
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        clientActionRepository.updateByChatId(chatId, new ClientAction(getCommand(), CONFIRM_ORDER_ACTION));

        sendConfirmOrderMessage(absSender, chatId);
    }

    private void sendConfirmOrderMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .parseMode("HTML")
                .text("Confirm order:")
                .replyMarkup(buildReplyKeyboardMarkup())
                .build();
        absSender.execute(message);
    }

    private ReplyKeyboardMarkup buildReplyKeyboardMarkup() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                KeyboardButton.builder().text(Button.ORDER_CONFIRM.getAlias()).build()
        )));

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                KeyboardButton.builder().text(Button.ORDER_STEP_CANCEL.getAlias()).build(),
                KeyboardButton.builder().text(Button.ORDER_STEP_PREVIOUS.getAlias()).build()
        )));
        return keyboardBuilder.build();
    }

    @Override
    public boolean canHandleAction(Update update, String action) {
        return update.hasMessage() && update.getMessage().hasText() && CONFIRM_ORDER_ACTION.equals(action);
    }

    @Override
    public void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if (!Button.ORDER_CONFIRM.getAlias().equals(text)) {
            sendNotCorrectConfirmOptionMessage(absSender, chatId);
            return;
        }

        completeOrder(absSender, update, chatId);
    }

    private void sendNotCorrectConfirmOptionMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("You have selected incorrect confirmation option, please press the button")
                .build();
        absSender.execute(message);
    }

    private void completeOrder(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        ClientOrder clientOrder = clientOrderStateRepository.findByChatId(chatId);

        Client client = clientRepository.findByChatId(chatId);
        client.setName(clientOrder.getClientName());
        client.setPhoneNumber(clientOrder.getPhoneNumber());
        client.setCity(clientOrder.getCity());
        client.setAddress(clientOrder.getAddress());

        Order order = new Order();
        order.setClient(client);
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING);
        order.setAmount(clientOrder.calculateTotalPrice());
        order.setItems(clientOrder.getCartItems().stream().map(OrderItem::from).toList());

        HibernateTransactionFactory.inTransactionVoid(session -> {
            orderRepository.save(order);
            clientRepository.update(order.getClient());
        });

        sendOrderMessage(absSender, chatId);
        clearClientOrderState(chatId);

        notificationService.notifyAdminChatAboutNewOrder(absSender, order);
    }

    private void sendOrderMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        String text = messageService.findByName("ORDER_CREATED_MESSAGE").buildText();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(Button.createGeneralMenuKeyboard())
                .build();
        absSender.execute(message);
    }

    private void clearClientOrderState(Long chatId) {
        clientActionRepository.deleteByChatId(chatId);
        clientCommandStateRepository.deleteAllByChatId(chatId);
        clientOrderStateRepository.deleteByChatId(chatId);
        cartRepository.deleteAllCartItemsByChatId(chatId);
    }

}