package ru.jmdevelop.besshoptgbot.handlers.commands.register;

import ru.jmdevelop.besshoptgbot.exceptions.HandlerNotFoundException;
import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.models.dom.Command;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CommandHandlerRegistryDefault implements CommandHandlerRegistry {

    private Map<Command, CommandHandler> commandHandlers;

    @Override
    public void setCommandHandlers(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers.stream().collect(toMap(CommandHandler::getCommand, identity()));
    }

    @Override
    public CommandHandler find(Command command) {
        CommandHandler commandHandler = commandHandlers.get(command);
        if (commandHandler == null) {
            throw new HandlerNotFoundException("CommandHandler with name '" + command + "' not found");
        }
        return commandHandler;
    }

}
