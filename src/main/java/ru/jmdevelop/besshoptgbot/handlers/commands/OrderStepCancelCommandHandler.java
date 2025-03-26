package ru.jmdevelop.besshoptgbot.handlers.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.models.dom.Button;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;
import ru.jmdevelop.besshoptgbot.repo.ClientOrderStateRepository;

public class OrderStepCancelCommandHandler implements UpdateHandler {

    private final ClientActionRepository clientActionRepository;
    private final ClientCommandStateRepository clientCommandStateRepository;
    private final ClientOrderStateRepository clientOrderStateRepository;

    public OrderStepCancelCommandHandler(
            ClientActionRepository clientActionRepository,
            ClientCommandStateRepository clientCommandStateRepository,
            ClientOrderStateRepository clientOrderStateRepository) {

        this.clientActionRepository = clientActionRepository;
        this.clientCommandStateRepository = clientCommandStateRepository;
        this.clientOrderStateRepository = clientOrderStateRepository;
    }

    @Override
    public Command getCommand() {
        return Command.ORDER_STEP_CANCEL;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().startsWith(Button.ORDER_STEP_CANCEL.getAlias());
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        clearClientOrderState(chatId);
        sendCanclelOrderMessage(absSender, chatId);
    }

    private void clearClientOrderState(Long chatId) {
        clientActionRepository.deleteByChatId(chatId);
        clientCommandStateRepository.deleteAllByChatId(chatId);
        clientOrderStateRepository.deleteByChatId(chatId);
    }

    private void sendCanclelOrderMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Order canceled.")
                .replyMarkup(Button.createGeneralMenuKeyboard())
                .build();
        absSender.execute(message);
    }

}