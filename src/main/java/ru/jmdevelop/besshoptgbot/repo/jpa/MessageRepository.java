package ru.jmdevelop.besshoptgbot.repo.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.models.entity.Message;

public interface MessageRepository  extends JpaRepository<Message, Long> {

    Message findByName(String messageName);

}