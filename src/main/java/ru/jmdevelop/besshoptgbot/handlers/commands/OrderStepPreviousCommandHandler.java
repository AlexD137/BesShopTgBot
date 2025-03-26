package ru.jmdevelop.besshoptgbot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.handlers.commands.register.CommandHandlerRegistry;
import ru.jmdevelop.besshoptgbot.models.dom.Button;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;

public class OrderStepPreviousCommandHandler implements UpdateHandler {

    private final CommandHandlerRegistry commandHandlerRegistry;
    private final ClientCommandStateRepository clientCommandStateRepository;

    public OrderStepPreviousCommandHandler(
            CommandHandlerRegistry commandHandlerRegistry,
            ClientCommandStateRepository clientCommandStateRepository) {

        this.commandHandlerRegistry = commandHandlerRegistry;
        this.clientCommandStateRepository = clientCommandStateRepository;
    }

    @Override
    public Command getCommand() {
        return Command.ORDER_STEP_PREVIOUS;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().startsWith(Button.ORDER_STEP_PREVIOUS.getAlias());
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        executePreviousCommand(absSender, update, chatId);
    }

    private void executePreviousCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        Command command = clientCommandStateRepository.popByChatId(chatId);
        if (command == null) {
            return;
        }

        CommandHandler commandHandler = commandHandlerRegistry.find(command);
        commandHandler.executeCommand(absSender, update, chatId);
    }

}