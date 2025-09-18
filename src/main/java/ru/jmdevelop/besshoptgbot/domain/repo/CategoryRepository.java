package ru.jmdevelop.besshoptgbot.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}