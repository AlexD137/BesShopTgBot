package ru.jmdevelop.besshoptgbot.domain.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}