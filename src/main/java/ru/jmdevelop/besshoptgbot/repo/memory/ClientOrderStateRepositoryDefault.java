package ru.jmdevelop.besshoptgbot.repo.memory;

import org.apache.commons.lang3.SerializationUtils;
import ru.jmdevelop.besshoptgbot.models.dom.ClientOrder;
import ru.jmdevelop.besshoptgbot.repo.ClientOrderStateRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientOrderStateRepositoryDefault implements ClientOrderStateRepository {

    private final Map<Long, ClientOrder> clientOrders = new ConcurrentHashMap<>();

    @Override
    public ClientOrder findByChatId(Long chatId) {
        ClientOrder clientOrder = clientOrders.get(chatId);
        return SerializationUtils.clone(clientOrder);
    }

    @Override
    public void updateByChatId(Long chatId, ClientOrder userOrder) {
        clientOrders.put(chatId, SerializationUtils.clone(userOrder));
    }

    @Override
    public void deleteByChatId(Long chatId) {
        clientOrders.remove(chatId);
    }

}