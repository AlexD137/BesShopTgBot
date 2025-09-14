package ru.jmdevelop.besshoptgbot.domain.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Client;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(Long id);
}