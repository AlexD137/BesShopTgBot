package ru.jmdevelop.besshoptgbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.models.entity.UserRequest;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
}
