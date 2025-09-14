package ru.jmdevelop.besshoptgbot.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.UserRequest;

import java.util.List;
import java.util.stream.Stream;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
}

