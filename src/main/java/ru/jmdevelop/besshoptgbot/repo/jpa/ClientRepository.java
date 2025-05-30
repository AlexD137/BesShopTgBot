package ru.jmdevelop.besshoptgbot.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.models.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByChatId(Long chatId);
}