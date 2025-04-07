package ru.jmdevelop.besshoptgbot.main;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.exceptions.HandlerNotFoundException;
import ru.jmdevelop.besshoptgbot.handlers.ActionHandler;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class TelegramBot extends TelegramLongPollingBot {




    private final Logger logger = LogManager.getLogger(getClass());

    private final String telegramBotUsername;
    private final ClientActionRepository clientActionRepository;
    private final Map<Command, UpdateHandler> updateHandlers;
    private final Map<Command, ActionHandler> actionHandlers;

    public TelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            ClientActionRepository clientActionRepository,
            List<UpdateHandler> updateHandlers,
            List<ActionHandler> actionHandlers) {

        super(new DefaultBotOptions(), botToken);
        this.telegramBotUsername = "telegram.bot.username";
        this.clientActionRepository = clientActionRepository;
        this.updateHandlers = updateHandlers.stream().collect(toMap(UpdateHandler::getCommand, identity()));
        this.actionHandlers = actionHandlers.stream().collect(toMap(ActionHandler::getCommand, identity()));
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            handle(update);
        } catch (Exception e) {
            logger.error("Failed to handle update", e);
        }
    }

    private void handle(Update update) throws TelegramApiException {
        if (handleCommand(update)) {
            return;
        }
        if (handleAction(update)) {
            return;
        }
    }

    private boolean handleCommand(Update update) throws TelegramApiException {
        List<UpdateHandler> foundCommandHandlers = updateHandlers.values().stream()
                .filter(commandHandler -> commandHandler.canHandleUpdate(update))
                .toList();

        if (foundCommandHandlers.size() > 1) {
            throw new HandlerNotFoundException("Found more than one command handler: " + foundCommandHandlers.size());
        }
        if (foundCommandHandlers.size() != 1) {
            return false;
        }

        foundCommandHandlers.get(0).handleUpdate(this, update);
        return true;
    }

    private boolean handleAction(Update update) throws TelegramApiException {
        if (!update.hasMessage()) {
            return false;
        }

        ClientAction clientAction = clientActionRepository.findByChatId(update.getMessage().getChatId());
        if (clientAction == null) {
            return false;
        }

        ActionHandler actionHandler = actionHandlers.get(clientAction.getCommand());
        if (actionHandler == null) {
            throw new HandlerNotFoundException("Failed to find action handler");
        }

        actionHandler.handleAction(this, update, clientAction.getAction());
        return true;
    }

}