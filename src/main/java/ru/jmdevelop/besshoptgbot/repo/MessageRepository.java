package ru.jmdevelop.besshoptgbot.repo;


import ru.jmdevelop.besshoptgbot.models.entity.Message;

public interface MessageRepository {

    Message findByName(String messageName);

}