package ru.jmdevelop.besshoptgbot.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.domain.models.user.Role;
import ru.jmdevelop.besshoptgbot.domain.models.user.User;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Client;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    // Специфичные методы для предметной области
    List<User> findActiveUsers();
    List<User> findUsersWithOrders();
    int countByRole(Role role);
}