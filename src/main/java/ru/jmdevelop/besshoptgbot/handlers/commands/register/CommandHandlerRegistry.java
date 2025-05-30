package ru.jmdevelop.besshoptgbot.handlers.commands.register;

import ru.jmdevelop.besshoptgbot.handlers.CommandHandler;
import ru.jmdevelop.besshoptgbot.models.dom.Command;

import java.util.List;

public interface CommandHandlerRegistry {
    CommandHandler find(Command command);
}