package ru.jmdevelop.besshoptgbot.services;

import ru.jmdevelop.besshoptgbot.models.entity.Message;

public interface MessageService {

    Message findByName(String messageName);

}
