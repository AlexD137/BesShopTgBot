package ru.jmdevelop.besshoptgbot.repo.memory;

import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCommandStateRepositoryDefault implements ClientCommandStateRepository {

    private final Map<Long, List<Command>> userCommands = new ConcurrentHashMap<>();

    @Override
    public void pushByChatId(Long chatId, Command command) {
        List<Command> commands = userCommands.computeIfAbsent(chatId, value -> new LinkedList<>());
        commands.add(command);
    }

    @Override
    public Command popByChatId(Long chatId) {
        List<Command> commands = userCommands.computeIfAbsent(chatId, value -> new LinkedList<>());
        if (commands.isEmpty()) {
            return null;
        }
        return commands.remove(commands.size() - 1);
    }

    @Override
    public void deleteAllByChatId(Long chatId) {
        userCommands.remove(chatId);
    }

}