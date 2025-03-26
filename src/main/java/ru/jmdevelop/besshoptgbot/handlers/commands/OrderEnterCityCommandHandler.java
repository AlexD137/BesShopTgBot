package ru.jmdevelop.besshoptgbot.handlers.commands;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.ActionHandler;
import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.register.CommandHandlerRegistry;
import ru.jmdevelop.besshoptgbot.models.dom.Button;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.models.dom.ClientOrder;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientOrderStateRepository;

import java.util.Arrays;
import java.util.regex.Pattern;

public class OrderEnterCityCommandHandler implements CommandHandler, ActionHandler {

    private static final String ENTER_CITY_ACTION = "order=enter-client-city";

    private static final Pattern CITY_PATTERN = Pattern.compile("[a-zA-Z]");

    private final CommandHandlerRegistry commandHandlerRegistry;
    private final ClientActionRepository clientActionRepository;
    private final ClientCommandStateRepository clientCommandStateRepository;
    private final ClientOrderStateRepository clientOrderStateRepository;

    public OrderEnterCityCommandHandler(
            CommandHandlerRegistry commandHandlerRegistry,
            ClientActionRepository clientActionRepository,
            ClientCommandStateRepository clientCommandStateRepository,
            ClientOrderStateRepository clientOrderStateRepository) {

        this.commandHandlerRegistry = commandHandlerRegistry;
        this.clientActionRepository = clientActionRepository;
        this.clientCommandStateRepository = clientCommandStateRepository;
        this.clientOrderStateRepository = clientOrderStateRepository;
    }

    @Override
    public Command getCommand() {
        return Command.ENTER_CITY;
    }

    @Override
    public void executeCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        clientActionRepository.updateByChatId(chatId, new ClientAction(getCommand(), ENTER_CITY_ACTION));

        sendEnterCityMessage(absSender, chatId);
        sendCurrentCityMessage(absSender, chatId);
    }

    private void sendEnterCityMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Enter your city")
                .replyMarkup(buildReplyKeyboardMarkup(false))
                .build();
        absSender.execute(message);
    }

    private void sendCurrentCityMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        ClientOrder clientOrder = clientOrderStateRepository.findByChatId(chatId);
        if (StringUtils.isBlank(clientOrder.getCity())) {
            return;
        }

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Current city: " + clientOrder.getCity())
                .replyMarkup(buildReplyKeyboardMarkup(true))
                .build();
        absSender.execute(message);
    }

    private ReplyKeyboardMarkup buildReplyKeyboardMarkup(boolean skip) {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        if (skip) {
            keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                    KeyboardButton.builder().text(Button.ORDER_STEP_NEXT.getAlias()).build()
            )));
        }

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                KeyboardButton.builder().text(Button.ORDER_STEP_CANCEL.getAlias()).build(),
                KeyboardButton.builder().text(Button.ORDER_STEP_PREVIOUS.getAlias()).build()
        )));
        return keyboardBuilder.build();
    }

    @Override
    public boolean canHandleAction(Update update, String action) {
        return update.hasMessage() && update.getMessage().hasText() && ENTER_CITY_ACTION.equals(action);
    }

    @Override
    public void handleAction(AbsSender absSender, Update update, String action) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if (Button.ORDER_STEP_NEXT.getAlias().equals(text)) {
            executeNextCommand(absSender, update, chatId);
            return;
        }
        if (!CITY_PATTERN.matcher(text).find()) {
            sendNotCorrectCityMessage(absSender, chatId);
            return;
        }

        saveClientOrderState(chatId, text);
        executeNextCommand(absSender, update, chatId);
    }

    private void sendNotCorrectCityMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("You entered the incorrect city, try again.")
                .build();
        absSender.execute(message);
    }

    private void saveClientOrderState(Long chatId, String text) {
        ClientOrder clientOrder = clientOrderStateRepository.findByChatId(chatId);
        clientOrder.setCity(text);
        clientOrderStateRepository.updateByChatId(chatId, clientOrder);
    }

    private void executeNextCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        clientCommandStateRepository.pushByChatId(chatId, getCommand());
        commandHandlerRegistry.find(Command.ENTER_ADDRESS).executeCommand(absSender, update, chatId);
    }

}