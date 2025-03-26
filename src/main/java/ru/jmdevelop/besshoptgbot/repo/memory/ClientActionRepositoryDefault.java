package ru.jmdevelop.besshoptgbot.repo.memory;

import org.apache.commons.lang3.SerializationUtils;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientActionRepositoryDefault implements ClientActionRepository {

    private final Map<Long, ClientAction> clientsAction = new ConcurrentHashMap<>();

    @Override
    public ClientAction findByChatId(Long chatId) {
        ClientAction clientAction = clientsAction.get(chatId);
        return SerializationUtils.clone(clientAction);
    }

    @Override
    public void updateByChatId(Long chatId, ClientAction clientAction) {
        clientsAction.put(chatId, SerializationUtils.clone(clientAction));
    }

    @Override
    public void deleteByChatId(Long chatId) {
        clientsAction.remove(chatId);
    }

}
