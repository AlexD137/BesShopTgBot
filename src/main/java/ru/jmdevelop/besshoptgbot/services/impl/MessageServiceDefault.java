package ru.jmdevelop.besshoptgbot.services.impl;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;
import ru.jmdevelop.besshoptgbot.models.entity.Message;
import ru.jmdevelop.besshoptgbot.repo.jpa.MessageRepository;
import ru.jmdevelop.besshoptgbot.repo.database.MessageRepositoryDefault;
import ru.jmdevelop.besshoptgbot.services.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MessageServiceDefault implements MessageService {

    private MessageRepository messageRepository = new MessageRepositoryDefault();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, Message> cachedMessages = new HashMap<>();

    public MessageServiceDefault() {
        startCacheClearTask();
    }

    private void startCacheClearTask() {
        executorService.scheduleAtFixedRate(cachedMessages::clear, 20, 20, TimeUnit.MINUTES);
    }

    public void setRepository(MessageRepository repository) {
        this.messageRepository = repository;
    }

    @Override
    public Message findByName(String messageName) {
        if (messageName == null) {
            throw new IllegalArgumentException("MessageName should not be NULL");
        }

        Message message = cachedMessages.get(messageName);
        if (message == null) {
            message = messageRepository.findByName(messageName);
            cachedMessages.put(messageName, message);
        }

        return SerializationUtils.clone(message);
    }

}